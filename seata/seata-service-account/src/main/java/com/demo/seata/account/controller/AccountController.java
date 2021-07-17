package com.demo.seata.account.controller;

import com.demo.seata.account.service.AccountService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
public class AccountController {
    @Resource
    private AccountService accountService;

    @PutMapping("/debit")
    public void debit(@RequestParam("userId") String userId, @RequestParam("money") BigDecimal money) {
        accountService.debit(userId, money);
    }
}
