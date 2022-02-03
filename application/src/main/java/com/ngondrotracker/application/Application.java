package com.ngondrotracker.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ngondrotracker.user", "com.ngondrotracker.token"})
@EnableJpaRepositories(basePackages = {"com.ngondrotracker.user.repository"})
@EntityScan(basePackages = {"com.ngondrotracker.user.ds"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
