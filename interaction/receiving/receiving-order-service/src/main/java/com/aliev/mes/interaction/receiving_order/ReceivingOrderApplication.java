package com.aliev.mes.interaction.receiving_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@ConfigurationPropertiesScan
@EnableFeignClients
@SpringBootApplication
public class ReceivingOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceivingOrderApplication.class, args);
    }
}