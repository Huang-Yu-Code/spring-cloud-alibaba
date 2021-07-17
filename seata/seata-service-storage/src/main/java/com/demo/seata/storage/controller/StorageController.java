package com.demo.seata.storage.controller;

import com.demo.seata.storage.service.StorageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class StorageController {
    @Resource
    private StorageService storageService;

    @PutMapping(value = "/deduct")
    public void deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count) {
        storageService.deduct(commodityCode, count);
    }

}
