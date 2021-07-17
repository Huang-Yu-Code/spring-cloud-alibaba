package com.demo.nacos.consumer.controller;

import com.demo.nacos.consumer.service.ConsumerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class ConsumerController {

    @Resource
    private ConsumerService consumerService;

    @GetMapping("/")
    public String index() {
        return "Nacos Discovery Consumer OpenFeign";
    }

    @GetMapping("/provider")
    public String provider() {
        return consumerService.getProvider();
    }

    @GetMapping("/exception")
    public String exception() {
        return consumerService.getException();
    }
}
