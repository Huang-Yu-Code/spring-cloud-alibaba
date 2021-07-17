package com.demo.dubbo.common.service;

public class CommonServiceMock implements CommonService {
    @Override
    public String sayHello(String name) {
        return "服务端发生异常，被降级了";
    }

    @Override
    public String getException() {
        return "服务端发生异常，被降级了";
    }
}
