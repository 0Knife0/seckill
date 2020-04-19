package com.seckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SuccessKilled {

    private long seckillId; // 秒杀商品id

    private long userPhone; // 用户phone

    private short state;    // -1:无效 0:成功 1:已付款 2:已发货

    private Date createTime;// 创建时间

    private Seckill seckill;// 多对一
}
