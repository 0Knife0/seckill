package com.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 暴露秒杀地址DTO
 */
@Data
public class Exposer {

    private boolean exposed;// 是否暴露地址

    private String md5;     // 一种加密措施

    private long seckillId;

    private long now;       // 系统当前时间（毫秒）

    private long start;

    private long end;
}
