package com.demo.sentinel.openfeign.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class BlockHandler {
    public static String defaultBlockHandler(BlockException exception){
        return "Sentinel Block";
    }
}
