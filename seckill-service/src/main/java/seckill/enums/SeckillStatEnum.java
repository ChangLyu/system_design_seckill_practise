package seckill.enums;

import seckill.entity.Seckill;

public enum SeckillStatEnum {
    SUCCESS(1, "Kill Success!"),
    END(0,"kill End!"),
    REPEAT_KILL(-1, "Kill Repeat!"),
    INNER_ERROR(-2,"System Error!"),
    DATA_REWRITE(-3,"Data Rewrite!");
    int state;
    String info;

    SeckillStatEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }
    public static SeckillStatEnum stateOf(int state){
        for(SeckillStatEnum value: values()){
            if(value.getState() == state){
                return value;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return info;
    }
}
