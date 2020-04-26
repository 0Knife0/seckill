package com.seckill.dao.cache;

import com.alibaba.fastjson.JSON;
import com.seckill.common.RedisConstants;
import com.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisDao {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 存入seckill
     * @param seckill
     */
    public void putSeckill(Seckill seckill){
        redisTemplate.opsForValue().set(RedisConstants.SECKILL_PREFIX + seckill.getSeckillId(),
                JSON.toJSONString(seckill), RedisConstants.SECKILL_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 取出seckill
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(Long seckillId){
        String secString = redisTemplate.opsForValue().get(RedisConstants.SECKILL_PREFIX + seckillId);
        return JSON.parseObject(secString, Seckill.class);
    }
}
