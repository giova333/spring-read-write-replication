package com.gladunalexander.readwritereplication;

import com.github.javafaker.Faker;
import com.gladunalexander.readwritereplication.user.User;
import com.gladunalexander.readwritereplication.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class ReadWriteReplicationApplication {

    @Bean
    SmartInitializingSingleton init(UserService service) {
        return () -> {
            var username = new Faker().name().username();
            service.save(new User(username));
            log.info("findByUsername: {}", service.findByUsername(username).orElseThrow());
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ReadWriteReplicationApplication.class, args);
    }

}