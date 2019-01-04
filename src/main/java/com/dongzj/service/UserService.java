package com.dongzj.service;

import com.dongzj.entity.User;

import java.util.Date;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/15
 * Time: 09:57
 */
public interface UserService {

    User genericUser(int id, String name, Long phone, Date birthDay);
}
