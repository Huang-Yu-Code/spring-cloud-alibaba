package com.demo.dubbo.consumer.service;

import com.demo.dubbo.common.service.CommonService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    @DubboReference
    private CommonService commonService;

    public String sayHello(){
        return commonService.sayHello("Dubbo-Nacos");
    }
}
