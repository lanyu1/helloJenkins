package com.lanyu.jenkins.hellojenkins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author lanyu
 * @date 2021年05月26日 14:32
 */
//启用缓存
@SpringBootApplication
//启用JPA审计
@EnableJpaAuditing
//启用缓存
@EnableCaching
//启用异步
@EnableAsync
//启用自带定时任务
@EnableScheduling
@CrossOrigin
public class HelloJenkinsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloJenkinsApplication.class, args);
    }

}
