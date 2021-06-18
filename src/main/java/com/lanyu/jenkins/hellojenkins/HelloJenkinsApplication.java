package com.lanyu.jenkins.hellojenkins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author lanyu
 * @date 2021年05月26日 14:32
 */
//启用缓存
@EnableCaching
@SpringBootApplication
public class HelloJenkinsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloJenkinsApplication.class, args);
    }

}
