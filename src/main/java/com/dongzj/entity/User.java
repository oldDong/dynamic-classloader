package com.dongzj.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/15
 * Time: 09:58
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -4288472790232606798L;

    private Integer id;

    private String name;

    private Long phone;

    private Date birthDay;
}
