package com.demo.seata.business.service;

import com.demo.seata.business.client.OrderClient;
import com.demo.seata.business.client.StorageClient;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class BusinessService {
    @Resource
    private OrderClient orderClient;

    @Resource
    private StorageClient storageClient;

    private final static String USERID = "1002";

    @GlobalTransactional(name = "business", rollbackFor = Exception.class)
    public void purchase(String userId, String commodityCode, int count) {
        System.out.println("business XID " + RootContext.getXID());
        orderClient.create(userId, commodityCode, count);
        storageClient.deduct(commodityCode, count);
        log.info("断点");
        if (userId.equals(USERID)){
            throw new RuntimeException("Business Exception");
        }
    }
}
