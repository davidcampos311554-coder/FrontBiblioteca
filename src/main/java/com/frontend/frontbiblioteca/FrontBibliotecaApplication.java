package com.frontend.frontbiblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FrontBibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontBibliotecaApplication.class, args);
    }

}
