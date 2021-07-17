package com.demo.nacos.config.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${config.info}")
    String info;

    @Value("${config.version}")
    String version;

    @GetMapping("/info")
    public String getInfo() {
        return info;
    }

    @GetMapping("/version")
    public String getVersion(){
        return  version;
    }
}
