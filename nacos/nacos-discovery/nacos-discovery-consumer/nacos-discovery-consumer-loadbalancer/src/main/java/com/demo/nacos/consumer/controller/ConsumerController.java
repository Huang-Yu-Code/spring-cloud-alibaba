package com.demo.nacos.consumer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class ConsumerController {

    static final String URL = "http://nacos-discovery-provider/";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String index(){
        return "Nacos Discovery Consumer LoadBalancer";
    }

    @GetMapping("/provider")
    public String provider() {
        return restTemplate.getForObject(URL + "provider", String.class);
    }

    @GetMapping("/exception")
    public String exception() {
        return restTemplate.getForObject(URL + "exception", String.class);
    }
}
