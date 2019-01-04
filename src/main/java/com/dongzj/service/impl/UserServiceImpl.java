package com.dongzj.service.impl;

import com.dongzj.entity.User;
import com.dongzj.service.UserService;

import java.util.Date;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/15
 * Time: 09:59
 */
public class UserServiceImpl implements UserService {

    public User genericUser(int id, String name, Long phone, Date birthDay) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setBirthDay(birthDay);
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
