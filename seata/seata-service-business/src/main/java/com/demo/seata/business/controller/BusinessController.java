package com.demo.seata.business.controller;

import com.demo.seata.business.service.BusinessService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class BusinessController {
    @Resource
    private BusinessService businessService;

    @Value("${seata.tx-service-group}")
    private String test;

    @GetMapping("/")
    public String index() {
        System.out.println(test);
        return "index";
    }

    @PostMapping("/purchase")
    @ResponseBody
    public void purchase(
            @RequestParam("userId") String userId,
            @RequestParam("commodityCode") String commodityCode,
            @RequestParam("count") int count) {
        businessService.purchase(userId, commodityCode, count);
    }
}
