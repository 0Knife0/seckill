package com.seckill.dao;

import com.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {

    // 增加秒杀记录
    int insertSuccessKilled(long seckillId, long userPhone, short state);

    // 根据id查询
    SuccessKilled selectByIdWithSeckill(long seckillId, long userPhone);
}
