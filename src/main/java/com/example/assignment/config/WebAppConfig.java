package com.example.assignment.config;

import com.example.assignment.WebAppInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = WebAppInitializer.class)
public class WebAppConfig {
    public WebAppConfig(){
        System.out.println("Hello Spring MVC");
    }
}