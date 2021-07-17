package com.demo.sentinel.ribbon.handler;

public class Fallback {
    public static String defaultFallback(){
        return "defaultFallback";
    }
}
