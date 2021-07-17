package com.demo.sentinel.ribbon.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.demo.sentinel.ribbon.handler.BlockHandler;
import com.demo.sentinel.ribbon.handler.Fallback;
import com.demo.sentinel.ribbon.service.SentinelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SentinelController {
    @Resource
    private SentinelService sentinelService;

    @GetMapping("/")
    public String index() {
        return "Hello Sentinel";
    }

    @GetMapping("/demo")
    @SentinelResource(
            value = "demo",
            blockHandlerClass = BlockHandler.class,
            blockHandler = "defaultBlockHandler",
            fallbackClass = Fallback.class,
            fallback = "defaultFallback")
    public String demo() {
        return sentinelService.demo();
    }

    @GetMapping("/demo1")
    @SentinelResource(
            value = "demo1",
            blockHandlerClass = BlockHandler.class,
            blockHandler = "defaultBlockHandler",
            fallbackClass = Fallback.class,
            fallback = "defaultFallback")
    public String demo1() {
        return sentinelService.demo1();
    }
}
