package com.demo.seata.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "seata-service-account")
public interface AccountClient {
    @PutMapping("/debit")
    void debit(@RequestParam("userId") String userId, @RequestParam("money") BigDecimal money);
}
