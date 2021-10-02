package seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
/**
 * 配置Spring和Junit整合,junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring的配置文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        successKilledDao.insertSuccessKilled(1000, 123456);
//        assertEquals(123456,successKilledDao.queryBySeckillId(1000,123456).getUserPhone());
    }
    @Test
    public void queryBySeckillId() {
        assertEquals(123456,successKilledDao.queryBySeckillId(1000,123456).getUserPhone());
    }
}