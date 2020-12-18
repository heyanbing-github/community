package com.jpx.entity;

public class Goods {

    private String gid;

    private Admin admin;

    private String gname;

    private String gphoto;

    private int gnum;

    private int price;  //商品价格

    private int count;    //上架的商品数

    private int score;        //购买获得的积分

    private int state;       //表示状态 0显示 1下架

    public Goods() {
    }

    public Goods(String gid, Admin admin, String gname, String gphoto, int gnum, int price, int count, int score, int state) {
        this.gid = gid;
        this.admin = admin;
        this.gname = gname;
        this.gphoto = gphoto;
        this.gnum = gnum;
        this.price = price;
        this.count = count;
        this.score = score;
        this.state = state;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGphoto() {
        return gphoto;
    }

    public void setGphoto(String gphoto) {
        this.gphoto = gphoto;
    }

    public int getGnum() {
        return gnum;
    }

    public void setGnum(int gnum) {
        this.gnum = gnum;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "gid='" + gid + '\'' +
                ", admin=" + admin +
                ", gname='" + gname + '\'' +
                ", gphoto='" + gphoto + '\'' +
                ", gnum=" + gnum +
                ", price=" + price +
                ", count=" + count +
                ", score=" + score +
                ", state=" + state +
                '}';
    }
}
