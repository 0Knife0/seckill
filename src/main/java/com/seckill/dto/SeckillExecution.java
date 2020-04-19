package com.seckill.dto;

import com.seckill.entity.SuccessKilled;
import lombok.Data;

/**
 * 封装秒杀执行后的结果
 */
@Data
public class SeckillExecution {

    private long seckillId;

    private int state;      // 秒杀执行结果

    private String stateInfo;   // 状态表示

    private SuccessKilled successKilled;
}
