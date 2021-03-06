package seckill.dao;

import org.apache.ibatis.annotations.Param;
import seckill.entity.Seckill;
import seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
    /**
     * Used combined key to filter out duplicate
     *
     * @param seckillId
     * @param userPhone
     * @return 返回的是表中的影响行数， insert records if return <1, then means failed
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * Query using seckillId to obtain its object
     *
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryBySeckillId(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
