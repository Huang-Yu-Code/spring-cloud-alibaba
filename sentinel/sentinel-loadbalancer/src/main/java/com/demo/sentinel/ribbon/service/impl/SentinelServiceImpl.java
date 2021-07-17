package com.demo.sentinel.ribbon.service.impl;

import com.demo.sentinel.ribbon.service.SentinelService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class SentinelServiceImpl implements SentinelService {
    static final String URL = "http://nacos-discovery-provider/";

    @Resource
    private RestTemplate restTemplate;

    public String demo() {
        return restTemplate.getForObject(URL+"demo", String.class);
    }

    public String demo1() {
        return restTemplate.getForObject(URL+"demo1", String.class);
    }
}
