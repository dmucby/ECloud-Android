package com.boyu.wang_pan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WangPanApplication {

    public static void main(String[] args) {
        SpringApplication.run(WangPanApplication.class, args);
    }

}
