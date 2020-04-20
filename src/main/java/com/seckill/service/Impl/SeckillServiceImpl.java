package com.seckill.service.Impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.findById(seckillId);
    }

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.findAll(0, 4);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        // 通过seckillId拿到秒杀项目seckill对象
        Seckill seckill = seckillDao.findById(seckillId);

        // 判断seckill是否为空，防止空指针异常
        if (seckill == null) {
            // 如果为空，返回false并返回秒杀项目id
            return new Exposer(false, seckillId);
            //return Exposer.builder().exposed(false).seckillId(seckillId).build();
        }

        // 如果不为空，拿到秒杀项目的开始时间和结束时间
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date now = new Date();

        // 比较当前系统时间是否在秒杀时间中，使用毫秒值进行比较
        if (now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime()) {
            // 如果不在秒杀时间，返回false
            return new Exposer(false, now.getTime(), startTime.getTime(), endTime.getTime());
           /* return Exposer.builder().exposed(false).now(now.getTime()).
                    start(startTime.getTime()).end(endTime.getTime()).build();*/
        }

        // 如果在秒杀时间中，返回true，md5和seckillId；
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
        //return Exposer.builder().exposed(true).md5(md5).seckillId(seckillId).build();
    }

    private String getMd5(long seckillId) {
        // 盐值，用于混淆，如果不混淆用户可以通过秒杀项目id自行生成md5
        String salt = "fja;dhogiHhboiodDJFHO^@KJGDSGIOHF342";
        String base = seckillId + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        // 首先判断md5是否合法，不合法抛出异常
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("秒杀失败，涉嫌篡改数据");
        }

        try {
            // 如果合法执行秒杀逻辑
            // 秒杀逻辑：减库存、如果成功插入秒杀成功记录
            Date nowTime = new Date();
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                // 没有更新到记录，秒杀关闭了
                throw new SeckillCloseException("秒杀结束了");
            } else {
                // 减库存成功，插入秒杀成功记录
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone, (short) 1);
                // 如果插入操作小于等于0，说明重复秒杀
                if (insertCount <= 0) {
                    throw new RepeatKillException("重复秒杀");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.selectByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                    /*return SeckillExecution.builder().seckillId(seckillId).
                            state(successKilled.getState()).
                            stateInfo(Objects.requireNonNull(SeckillStateEnum.stateOf(successKilled.getState()).getStateInfo())).
                            successKilled(successKilled).build();*/
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译器异常转化为运行期异常
            throw new SeckillException("秒杀内部错误" + e.getMessage());
        }
    }
}