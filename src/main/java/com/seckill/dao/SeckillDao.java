package com.seckill.dao;

import com.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    // 减少库存
    int reduceNumber(long seckillId, Date killTime);

    // 根据id查询
    Seckill findById(long seckillId);

    // 分页查询
    List<Seckill> findAll(int offset, int limit);

}
