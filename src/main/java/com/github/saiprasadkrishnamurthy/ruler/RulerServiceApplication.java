package com.github.saiprasadkrishnamurthy.ruler;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableMongoRepositories
@EnableCaching
@EnableEncryptableProperties
@EnableScheduling
@SpringBootApplication
public class RulerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RulerServiceApplication.class, args);
    }
}
