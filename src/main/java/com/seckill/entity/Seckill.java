package com.seckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Seckill {

    private long seckillId;     // 商品库存id

    private String name;        // 商品名称

    private int number;         // 库存数量

    private Date createTime;    // 创建时间

    private Date startTime;     // 秒杀开始时间

    private Date endTime;       // 秒杀结束时间
}
