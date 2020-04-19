package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() {
        seckillDao.reduceNumber(1000, new Date());
    }

    @Test
    public void findById() {
        Seckill seckill = seckillDao.findById(1000);
        System.out.println(seckill);
    }

    @Test
    public void findAll() {
        List<Seckill> list = seckillDao.findAll(0, 2);
        System.out.println(list);
    }
}