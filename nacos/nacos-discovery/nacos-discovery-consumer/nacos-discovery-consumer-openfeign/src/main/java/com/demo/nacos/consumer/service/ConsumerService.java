package com.demo.nacos.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "nacos-discovery-provider")
public interface ConsumerService {
    @GetMapping("/")
    String getIndex();

    @GetMapping("/provider")
    String getProvider();

    @GetMapping("/exception")
    String getException();
}
