package com.demo.seata.storage.service;

import com.demo.seata.storage.entity.Storage;
import com.demo.seata.storage.mapper.StorageMapper;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class StorageService {
    @Resource
    private StorageMapper storageMapper;

    @Transactional(rollbackFor = Exception.class)
    public void deduct(String commodityCode, int count) {
        System.out.println("storage XID " + RootContext.getXID());
        Storage storage = storageMapper.findByCommodityCode(commodityCode);
        storage.setCount(storage.getCount() - count);
        storageMapper.updateById(storage);
    }
}
