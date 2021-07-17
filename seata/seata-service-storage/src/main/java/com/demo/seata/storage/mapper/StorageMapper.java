package com.demo.seata.storage.mapper;

import com.demo.seata.storage.entity.Storage;
import org.apache.ibatis.annotations.Param;

public interface StorageMapper {

    Storage findByCommodityCode(@Param("commodityCode") String commodityCode);

    int updateById(Storage record);

}
