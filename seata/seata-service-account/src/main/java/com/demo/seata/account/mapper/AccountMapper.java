package com.demo.seata.account.mapper;

import com.demo.seata.account.entity.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {
    Account selectByUserId(@Param("userId") String userId);

    int updateById(Account account);
}
