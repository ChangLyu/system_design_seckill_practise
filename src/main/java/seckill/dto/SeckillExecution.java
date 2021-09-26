package seckill.dto;

import seckill.entity.SuccessKilled;
import seckill.enums.SeckillStatEnum;

public class SeckillExecution {
    private long seckillId;
    // result state,
    private SeckillStatEnum state;
    private String stateInfo;
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillId, SeckillStatEnum state, String stateInfo, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum state, String stateInfo) {
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public SeckillStatEnum getState() {
        return state;
    }

    public void setState(SeckillStatEnum state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
