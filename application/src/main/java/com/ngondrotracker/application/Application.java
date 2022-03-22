package com.ngondrotracker.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.ngondrotracker.common",
        "com.ngondrotracker.user",
        "com.ngondrotracker.token",
        "com.ngondrotracker.meditation"
})
@EnableJpaRepositories(basePackages = {
        "com.ngondrotracker.user.repository",
        "com.ngondrotracker.meditation.repository"
})
@EntityScan(basePackages = {
        "com.ngondrotracker.user.entity",
        "com.ngondrotracker.meditation.entity"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
