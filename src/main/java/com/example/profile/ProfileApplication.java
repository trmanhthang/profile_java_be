package com.example.profile;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;

@EnableCaching
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class ProfileApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProfileApplication.class, args);
    }
    private final LocalDate date = LocalDate.now();

    @Override
    public void run(String... args) {
        System.out.println();
        System.out.println("----------------------------------------------------------");
        System.out.println("---------------PROFILE APPLICATION STARTED---------------");
        System.out.println("--------------- " + date + " ---------------");
        System.out.println();
    }
}
