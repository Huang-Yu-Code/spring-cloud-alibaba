package com.demo.dubbo.zookeeper.provider.service;

import com.demo.dubbo.common.service.CommonService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@DubboService
@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public String sayHello(String name) {
        return name;
    }

    @Override
    public String getException() {
        return 1 / 0 + "";
    }
}
