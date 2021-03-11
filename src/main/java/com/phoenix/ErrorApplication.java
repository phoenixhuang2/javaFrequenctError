package com.phoenix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories
@EntityScan(value = {"com.phoenix.springtransaction"})
public class ErrorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErrorApplication.class, args);
    }

}
