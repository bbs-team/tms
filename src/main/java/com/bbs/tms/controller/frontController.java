package com.bbs.tms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class frontController{
    
    @RequestMapping(value="/test")
    public String main(){
        return "index";
    }
    
}