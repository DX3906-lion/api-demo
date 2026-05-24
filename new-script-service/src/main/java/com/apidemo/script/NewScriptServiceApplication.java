package com.apidemo.script;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.apidemo.script", "com.apidemo.common"})
public class NewScriptServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewScriptServiceApplication.class, args);
    }
}
