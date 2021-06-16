 package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author admin
 * @date 2021/06/14
 */
@Controller
public class AdminController {

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }
    
    @RequestMapping("/toSuccess")
    public String toIndex() {
        return "success";
    }
     
}
