package com.demo.nacos.provider.service.impl;

import com.demo.nacos.provider.service.ProviderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Value("${spring.application.name}")
    private String name;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String address;

    @Value("${server.port}")
    private int port;

    @Override
    public String index() {
        return "Nacos Discovery Provider";
    }

    @Override
    public String getProvider() {
        return name + "---" + address + "---" + port;
    }

    @Override
    public String getException() {
        throw new RuntimeException("服务异常");
    }
}
