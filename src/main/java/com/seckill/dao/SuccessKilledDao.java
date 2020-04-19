package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledDao {

    // 增加秒杀记录
    int insertSuccessKilled(@Param("seckillId") long seckillId,
                            @Param("userPhone") long userPhone,
                            @Param("state") short state);

    // 根据id查询
    SuccessKilled selectByIdWithSeckill(@Param("seckillId") long seckillId,
                                        @Param("userPhone") long userPhone);
}
