package seckill.service;

import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.entity.Seckill;
import seckill.exception.RepeatKillException;
import seckill.exception.SeckillCloseException;
import seckill.exception.SeckillException;

import java.util.List;

public interface SeckillService {
    List<Seckill> getSeckillList();

    Seckill getById(long seckillId);

    Exposer exportSeckillUrl(long seckillId);

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;

}
