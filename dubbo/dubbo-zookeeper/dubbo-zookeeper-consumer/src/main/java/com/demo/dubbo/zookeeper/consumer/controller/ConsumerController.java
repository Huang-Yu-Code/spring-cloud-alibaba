package com.demo.dubbo.zookeeper.consumer.controller;

import com.demo.dubbo.zookeeper.consumer.service.ConsumerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ConsumerController {

    @Resource
    private ConsumerService consumerService;

    @GetMapping("/")
    public String sayHello(){
        return consumerService.sayHello();
    }

    @GetMapping("/exception")
    public String getException(){
        return consumerService.getException();
    }
}
