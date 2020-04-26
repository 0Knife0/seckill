package com.seckill.controller;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    // 列表页
    // GET http:127.0.0.1:8080/seckill/list
    @GetMapping("/list")
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    // 详情页
    // GET http:127.0.0.1:8080/seckill/{seckillId}/detail
    @GetMapping("/{seckillId}/detail")
    public String detail(@PathVariable Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    // ajax接口，返回json
    // POST http:127.0.0.1:8080/seckill/{seckillId}/exposer
    @ResponseBody
    @PostMapping("/{seckillId}/exposer")
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId) {
        /**
         * 先试用seckillId查redis中是否有数据
         * 有：直接返回
         * 没有：从mysql中取出，并放入redis中
         */


        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    // @CookieValue(required = false)表示不必须有此项参数
    // 秒杀执行
    // POST http:127.0.0.1:8080/seckill/{seckillId}/{md5}/execution
    @ResponseBody
    @PostMapping("/{seckillId}/{md5}/execution")
    public SeckillResult<SeckillExecution> execution(
            @PathVariable Long seckillId,
            @PathVariable String md5,
            @CookieValue(required = false) Long userPhone
    ) {
        if (userPhone == null) {
            return new SeckillResult<>(false, "未知用户");
        }
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResult<>(true, seckillExecution);
        } catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(false, seckillExecution);
        } catch (SeckillCloseException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(false, seckillExecution);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(false, seckillExecution);
        }
    }

    // 获取系统时间
    // GET http:127.0.0.1:8080/seckill/time/now
    @ResponseBody
    @GetMapping("/time/now")
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}
