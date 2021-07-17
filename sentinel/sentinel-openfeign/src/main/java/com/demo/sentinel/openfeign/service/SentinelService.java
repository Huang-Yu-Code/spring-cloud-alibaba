package com.demo.sentinel.openfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "nacos-discovery-provider")
public interface SentinelService {
    @GetMapping("/demo")
    String demo();

    @GetMapping("/demo1")
    String demo1();
}
