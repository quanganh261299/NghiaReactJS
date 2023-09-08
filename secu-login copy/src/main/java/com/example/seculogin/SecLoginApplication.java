package com.example.seculogin;

import com.github.javafaker.Faker;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SecLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecLoginApplication.class, args);
    }

    @Bean
    public Faker faker() {
        return new Faker();
    }



}
