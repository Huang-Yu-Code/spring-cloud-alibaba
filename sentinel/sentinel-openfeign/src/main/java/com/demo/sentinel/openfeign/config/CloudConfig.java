package com.demo.sentinel.openfeign.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
@EnableFeignClients("com.demo.sentinel.openfeign.service")
public class CloudConfig {
}
