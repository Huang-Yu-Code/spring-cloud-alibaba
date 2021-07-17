package com.demo.seata.order.service;

import com.demo.seata.order.client.AccountClient;
import com.demo.seata.order.entity.Order;
import com.demo.seata.order.mapper.OrderMapper;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private AccountClient accountClient;

    @Transactional(rollbackFor = Exception.class)
    public void create(String userId, String commodityCode, Integer count) {
        System.out.println("order XID " + RootContext.getXID());
        BigDecimal money = new BigDecimal(count).multiply(new BigDecimal(10));
        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(money);
        orderMapper.insert(order);
        accountClient.debit(userId, money);
    }
}
