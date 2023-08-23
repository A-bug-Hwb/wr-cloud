package com.wr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WrGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WrGateWayApplication.class, args);
        System.out.println("Wr网关中心启动成功");
    }
}
