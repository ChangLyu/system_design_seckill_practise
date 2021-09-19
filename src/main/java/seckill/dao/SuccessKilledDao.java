package seckill.dao;

import seckill.entity.Seckill;
import seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
    /**
     * Used combined key to filter out duplicate
     * @param seckillId
     * @param userPhone
     * @return insert records if return <1, then means failed
     */
    int insertSuccessKilled(long seckillId, long userPhone);

    /**
     *  Query using seckillId to obtain its object
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryBySeckillId(long seckillId, long userPhone);
}
