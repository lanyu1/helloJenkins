package com.lanyu.jenkins.hellojenkins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author lanyu
 * @date 2021年05月26日 14:32
 */
//启用缓存
@EnableCaching
@SpringBootApplication
@CrossOrigin
public class HelloJenkinsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloJenkinsApplication.class, args);
    }

}
