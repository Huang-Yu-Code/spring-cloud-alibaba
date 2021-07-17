package com.demo.sentinel.openfeign.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.demo.sentinel.openfeign.handler.BlockHandler;
import com.demo.sentinel.openfeign.handler.Fallback;
import com.demo.sentinel.openfeign.service.SentinelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/demo2")
    @SentinelResource(
            value = "demo2",
            blockHandlerClass = BlockHandler.class,
            blockHandler = "defaultBlockHandler",
            fallbackClass = Fallback.class,
            fallback = "defaultFallback")
    public String demo2() {
        return sentinelService.demo1();
    }

    @GetMapping("/demo3")
    @SentinelResource(
            value = "demo3",
            blockHandlerClass = BlockHandler.class,
            blockHandler = "defaultBlockHandler",
            fallbackClass = Fallback.class,
            fallback = "defaultFallback")
    public String demo3() {
        return sentinelService.demo1();
    }

    @GetMapping("/demo4")
    @SentinelResource(
            value = "demo4",
            blockHandlerClass = BlockHandler.class,
            blockHandler = "defaultBlockHandler",
            fallbackClass = Fallback.class,
            fallback = "defaultFallback")
    public String demo4(@RequestParam(value = "key", required = false) String key) {
        return "HostKey: "+ key;
    }

}
