 package com.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author admin
 * @date 2021/06/15
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String helloWorld() {
        return "HelloWorld";
    }
    
    @Secured("ROLE_admin")
    @GetMapping("/password")
    public String getPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passHash = encoder.encode(password);
        System.out.println(passHash);

        boolean matches = encoder.matches(password, passHash);
        System.out.println(matches);

        return passHash;
    }
    
}
