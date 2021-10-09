package seckill.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import seckill.dao.SeckillDao;
import seckill.dao.SuccessKilledDao;
import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.entity.Seckill;
import seckill.entity.SuccessKilled;
import seckill.enums.SeckillStatEnum;
import seckill.exception.RepeatKillException;
import seckill.exception.SeckillCloseException;
import seckill.exception.SeckillException;
import seckill.service.SeckillService;

import java.util.Date;
import java.util.List;

@Service
public class SeckillServcieImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    // used for genereate MD5;
    private final String slat = "IUYGHJI*&^TYU$%^&*OJ)(23456789JBU)";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) return new Exposer(false, seckillId);
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.before(startTime) || nowTime.after(endTime)) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        return new Exposer(true, getMD5(seckillId), seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
    }

    /**
     * Java异常分编译期异常和运行期异常，运行期异常不需要手工try-catch，Spring的的声明式事务只接收运行期异常回滚策略，非运行期异常不会帮我们回滚。
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !getMD5(seckillId).equals(md5)) {
            throw new SeckillException("Seckill rewrite"); // user don't have correct md5
        }
        Date killTime = new Date();
        try {
            int updateContent = seckillDao.reduceNumber(seckillId, killTime);
            if (updateContent <= 0) {
                throw new SeckillCloseException("seckill closed");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated!");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryBySeckillId(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("Seckill Inner Error:" + e.getMessage());
        }
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
