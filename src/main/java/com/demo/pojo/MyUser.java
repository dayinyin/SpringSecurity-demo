 package com.demo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author admin
 * @date 2021/06/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("springsecurity_user")
public class MyUser {
    
    Integer id;
    String username;
    String password;
    String role;

}
