package com.jpx.dto;

import com.jpx.entity.Goods;

public class UserGoodsDto {
    private int guId;

    private Goods goods ;

    private int state; //0显示  1删除

    private int num;

    private String uid;

    private String buyTime;

    public UserGoodsDto() {
    }

    public UserGoodsDto(int guId, Goods goods, int state, int num, String uid, String buyTime) {
        this.guId = guId;
        this.goods = goods;
        this.state = state;
        this.num = num;
        this.uid = uid;
        this.buyTime = buyTime;
    }

    public int getGuId() {
        return guId;
    }

    public void setGuId(int guId) {
        this.guId = guId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    @Override
    public String toString() {
        return "UserGoodsDto{" +
                "guId=" + guId +
                ", goods=" + goods +
                ", state=" + state +
                ", num=" + num +
                ", uid='" + uid + '\'' +
                ", buyTime='" + buyTime + '\'' +
                '}';
    }
}
