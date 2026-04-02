
package com.medreminder.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedCareApi {
    public static void main(String[] args) {
        SpringApplication.run(MedCareApi.class, args);
    }
}
