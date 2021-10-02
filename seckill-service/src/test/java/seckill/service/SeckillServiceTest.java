package seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.entity.Seckill;
import seckill.exception.RepeatKillException;
import seckill.exception.SeckillCloseException;
import seckill.exception.SeckillException;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * 配置Spring和Junit整合,junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}", seckillList);
        assertEquals(seckillList.size(), 4);
    }

    @Test
    public void getById() {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckill is: {}", seckill);
        assertEquals(seckill.getSeckillId(), 1000);
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer = seckillService.exportSeckillUrl(1000);
        logger.info("exposure is: {}", exposer);
        assertEquals(exposer.isExposed(), false);
    }

    @Transactional
    @Test(expected = SeckillException.class)
    public void executeSeckillRewriteException() {
        long seckillId = 1000;
        long userPhone = 13476191876L;
        String md5 = "6789JBU)";
        SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
        logger.info("result={}", execution);
    }

    @Transactional
    @Test
    public void executeSeckillCLoseRepeatException() {
        long seckillId = 1000;
        long userPhone = 13476191876L;
        String md5 = "91462fb2a842228354a60135d4b2fb69";
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            logger.info("result={}", execution);
        } catch (SeckillCloseException e) {
            logger.info(e.getMessage());
            assertEquals(e.getMessage(),"seckill closed");
        }
    }

}