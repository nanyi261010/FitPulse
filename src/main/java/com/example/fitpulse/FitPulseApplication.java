package com.example.fitpulse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.fitpulse.Mapper")
public class FitPulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitPulseApplication.class, args);
    }

}
