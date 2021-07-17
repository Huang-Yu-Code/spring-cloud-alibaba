package com.demo.dubbo.zookeeper.consumer.service;

import com.demo.dubbo.common.service.CommonService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    @DubboReference(check = false, mock = "fail:true")
    private CommonService commonService;

    public String sayHello(){
        return commonService.sayHello("Dubbo Zookeeper Provider");
    }

    public String getException(){
        return commonService.getException();
    }
}
