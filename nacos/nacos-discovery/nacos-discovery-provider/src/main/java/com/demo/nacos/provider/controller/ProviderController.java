package com.demo.nacos.provider.controller;

import com.demo.nacos.provider.service.ProviderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProviderController {

    @Resource
    private ProviderService providerService;

    @GetMapping("/")
    public String index() {
        return providerService.index();
    }

    @GetMapping("/provider")
    public String getProvider(){
        return providerService.getProvider();
    }

    @GetMapping("/exception")
    public String getException() {
        return providerService.getException();
    }
}
