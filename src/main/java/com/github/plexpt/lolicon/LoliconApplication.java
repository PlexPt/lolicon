package com.github.plexpt.lolicon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class LoliconApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoliconApplication.class, args);
    }

}
