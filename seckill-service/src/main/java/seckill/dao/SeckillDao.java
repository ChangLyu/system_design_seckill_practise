package seckill.dao;

import org.apache.ibatis.annotations.Param;
import seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /**
     * update inventory (decrease)
     *
     * @param seckillId
     * @param killTime  it is the time user trigger this action, only valid after start time
     * @return update record, if <1, then means failed
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * query seckill inventroy by Id
     *
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * query list of seckill by offset and limit
     *
     * @param offset , skipped offset count of records
     * @param limit  , return limit count of records
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

}
