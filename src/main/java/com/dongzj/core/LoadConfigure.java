package com.dongzj.core;

import com.dongzj.entity.Global;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 加载配置文件，并实例化服务
 * <p>
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/15
 * Time: 10:44
 */
public class LoadConfigure {

    /**
     * 加载该服务下的配置，并实例化相关内容
     *
     * @param serviceRootPath
     */
    public static void load(String serviceRootPath) throws Exception {
        String serviceLib = serviceRootPath + File.separator + "lib";
        String serviceConf = serviceRootPath + File.separator + "conf";

        //读取配置文件
        File confFile = new File(serviceConf + File.separator + "service.xml");
        SAXReader reader = new SAXReader();
        Document document = reader.read(confFile);
        document.setXMLEncoding("UTF-8");
        Element root = document.getRootElement();

        //读取property
        Element proNode = root.element("property");
        Element connectionNode = proNode.element("connection");
        Element nettyNode = proNode.element("netty");

        Global.getInstance().setIp(connectionNode.attributeValue("ip"));
        Global.getInstance().setPort(Integer.parseInt(connectionNode.attributeValue("port")));
        Global.getInstance().setTimeout(Integer.parseInt(connectionNode.attributeValue("timeout")));
        Global.getInstance().setMaxBuf(Integer.parseInt(nettyNode.attributeValue("maxBuf")));


        //读取services
        final Map<String, String> serviceMap = new HashMap<String, String>();
        Element servicesNode = root.element("services");
        List<Element> servicesList = servicesNode.elements("service");
        servicesList.stream().forEach(e -> serviceMap.put(e.attributeValue("name"), e.attributeValue("impl")));

        //实例化相关类
        initService(serviceMap, serviceLib);
        System.out.println("Global: " + Global.getInstance().toString());
    }

    /**
     * 加载该服务所有的jar,并实例化jar中是所有的实现
     *
     * @param services
     * @param serviceLibPath
     */
    private static void initService(Map<String, String> services, String serviceLibPath) throws Exception {
        File serviceLibDir = new File(serviceLibPath);
        File[] jarFiles = serviceLibDir.listFiles((File dir, String name) -> name.endsWith(".jar"));
        if (jarFiles == null) {
            System.out.println(".jar file not exist");
            return;
        }
        URL[] jarUrls = new URL[jarFiles.length];
        for (int i = 0; i < jarUrls.length; i++) {
            System.out.println("加载的类有：" + jarFiles[i].getName());
            jarUrls[i] = jarFiles[i].toURI().toURL();
        }

        URLClassLoader classLoader = new URLClassLoader(jarUrls, ClassLoader.getSystemClassLoader());

        /**
         * 懒加载模式，在启动服务时，初始化所有实现类
         */
        Map<String, Object> instances = new HashMap<>();
        Map<String, Class> types = new HashMap<>();
        Iterator<Map.Entry<String, String>> it = services.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            Class clazz = classLoader.loadClass(entry.getValue());
            instances.put(entry.getKey(), clazz.newInstance());
            types.put(entry.getKey(), clazz);
        }
        Global.getInstance().setClassLoader(classLoader);
        Global.getInstance().setServiceImpl(instances);
        Global.getInstance().setServiceClass(types);
    }

    public static void main(String[] args) {
        String serviceRootPath = "src/main/resources";
        try {
            load(serviceRootPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
