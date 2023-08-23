package com.wr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WrUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(WrUserApplication.class, args);
        System.out.println("Wr用户中心启动成功");
    }
}
