package com.jpx.dto;

public class TopGoodsDto {
    private String gname;
    private int value;

    public TopGoodsDto(){}

    public TopGoodsDto(String gname, int value) {
        this.gname = gname;
        this.value = value;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TopGoodsDto{" +
                "gname='" + gname + '\'' +
                ", value=" + value +
                '}';
    }
}
