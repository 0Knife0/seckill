package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        long seckillId = 1000L;
        long userPhone = 13750010101L;
        short state = 1;
        successKilledDao.insertSuccessKilled(seckillId, userPhone, state);
    }

    @Test
    public void selectByIdWithSeckill() {
        long seckillId = 1000L;
        long userPhone = 13750010101L;
        SuccessKilled successKilled = successKilledDao.selectByIdWithSeckill(seckillId, userPhone);
        System.out.println(successKilled);
    }
}