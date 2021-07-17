package com.demo.sentinel.openfeign.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Fallback {
    public static String defaultFallback() {
        log.info("openfeign fail");
        return "defaultFallback";
    }
}
