package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    // 减少库存
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    // 根据id查询
    Seckill findById(@Param("seckillId") long seckillId);

    // 分页查询
    List<Seckill> findAll(@Param("offset") int offset,@Param("limit") int limit);

}
