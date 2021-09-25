package seckill.service;

import seckill.entity.Seckill;

import java.util.List;

public interface SeckillService {
    List<Seckill> getSeckillList();
    Seckill getById(long seckillId);
    void exportSeckillUrl(long seckillId);
    
}
