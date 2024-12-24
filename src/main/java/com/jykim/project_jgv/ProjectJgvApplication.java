package com.jykim.project_jgv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ProjectJgvApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectJgvApplication.class, args);
    }

}
