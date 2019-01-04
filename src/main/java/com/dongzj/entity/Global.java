package com.dongzj.entity;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/15
 * Time: 10:37
 */
@Data
public class Global {

    /**
     * 静态内部类实现线程安全的单例
     */
    private static class SingleHolder {
        private static final Global INSTANCE = new Global();
    }

    public static Global getInstance() {
        return SingleHolder.INSTANCE;
    }

    /**
     * 网络连接配置信息
     */
    private String ip;
    private Integer port;
    private Integer timeout;

    /**
     * netty缓冲区的默认大小
     */
    private Integer maxBuf = 1024;

    /**
     * 服务名称，对应service.xml中的name节点
     */
    private String serviceName;

    /**
     * 服务缓存
     */
    private Map<String, Object> serviceImpl;

    /**
     * 服务实现类缓存
     */
    private Map<String, Class> serviceClass;

    /**
     * 方法缓存
     */
    private Map<String, Method> methodCache;

    private ClassLoader classLoader;

    private Global() {
        methodCache = new ConcurrentHashMap<String, Method>();
    }

    public Method getMethod(String serviceName, String methodName, List<String> paramsTypesName) {
        return this.methodCache.get(buildKey(serviceName, methodName, paramsTypesName));
    }

    public void putMethod(String serviceName, String methodName, List<String> paramsTypesName, Method method) {
        this.methodCache.put(buildKey(serviceName, methodName, paramsTypesName), method);
    }

    private String buildKey(String serviceName, String methodName, List<String> paramsTypeName) {
        StringBuilder methodKey = new StringBuilder(serviceName);
        methodKey.append("-").append(methodName);
        for (String s : paramsTypeName) {
            methodKey.append("-").append(s);
        }
        return methodKey.toString();
    }

}
