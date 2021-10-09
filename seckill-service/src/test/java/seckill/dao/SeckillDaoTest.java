package seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import seckill.entity.Seckill;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置Spring和Junit整合,junit启动时加载springIOC容器 spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring的配置文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SeckillDaoTest {

    // 注入Dao实现类依赖
    // 当看到resource会在spring池中查找实现类并且注入
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws ParseException {
        int id = 1000;
        Date killDate = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse("2016-01-01 00:20:00");
        int initialValue = seckillDao.queryById(id).getNumber();
        seckillDao.reduceNumber(id, killDate);
        int remainingValue = seckillDao.queryById(id).getNumber();
        assertEquals(initialValue-1,remainingValue);
    }

    @Test
    public void queryById() {
        long id =1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for(Seckill seckill: seckills){
            System.out.print(seckill);
        }
    }
}