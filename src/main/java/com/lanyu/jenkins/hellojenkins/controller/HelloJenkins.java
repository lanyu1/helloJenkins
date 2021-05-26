package com.lanyu.jenkins.hellojenkins.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lanyu
 * @date 2021年05月26日 14:32
 */
@RestController
@RequestMapping("/jenkins")
public class HelloJenkins {

    @RequestMapping("/helloJenkins")
    public String helloJenkins(){
        return "Hello Jenkins";
    }
}
