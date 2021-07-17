package com.demo.dubbo.provider.service;

import com.demo.dubbo.common.service.CommonService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService
public class CommonServiceImpl implements CommonService {
    @Override
    public String sayHello(String name) {
        return name;
    }

    @Override
    public String getException() {
        return null;
    }
}
