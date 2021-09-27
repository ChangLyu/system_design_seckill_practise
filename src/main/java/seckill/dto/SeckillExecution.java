package seckill.dto;

import seckill.entity.SuccessKilled;
import seckill.enums.SeckillStatEnum;

public class SeckillExecution {
    private long seckillId;
    // result state,
    private int state;
    private String stateInfo;
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillId, SeckillStatEnum state,  SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = state.getState();
        this.stateInfo = state.getInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum state) {
        this.seckillId = seckillId;
        this.state = state.getState();
        this.stateInfo = state.getInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
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
