package com.demo.seata.account.service;

import com.demo.seata.account.entity.Account;
import com.demo.seata.account.mapper.AccountMapper;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class AccountService {
    @Resource
    private AccountMapper accountMapper;

    @Transactional(rollbackFor = Exception.class)
    public void debit(String userId, BigDecimal money) {
        System.out.println("account XID " + RootContext.getXID());
        Account account = accountMapper.selectByUserId(userId);
        account.setMoney(account.getMoney().subtract(money));
        accountMapper.updateById(account);
    }
}
