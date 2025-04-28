package com.warlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.warlock.domain")
@EnableJpaRepositories("com.warlock.repository")
@EnableScheduling
public class ExtinctsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtinctsWebApplication.class, args);
    }
}