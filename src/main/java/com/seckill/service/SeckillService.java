package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在'使用者'的角度设计接口
 * 三个方面：方法定义粒度、参数、返回类型（return 类型/异常）
 */
public interface SeckillService {

    // 查询一个秒杀记录
    Seckill getById(long seckillId);

    // 查询所有秒杀记录
    List<Seckill> getSeckillList();

    // 输出秒杀开启时秒杀接口地址、否则输出系统时间和秒杀时间
    Exposer exportSeckillUrl(long seckillId);

    // 执行秒杀操作
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;

    // 执行秒杀操作by存储过程
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
