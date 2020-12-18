package com.jpx.entity;

public class File {

    private String fid;

    private String fname;//文件名

    private  String sendTime;//发帖时间

    private String context;//标题

    private  User user;//作者

    private int score;//基本积分

    private int size;//文件大小

    private int ispass;//审核情况 0审核通过 1审核等待 2审核不通

    private int state;//#0显示 1删除

    private int loadCount;//

    public File() {
    }

    public File(String fid, String fname, String sendTime, String context, User user, int score, int size, int ispass, int state, int loadCount) {
        this.fid = fid;
        this.fname = fname;
        this.sendTime = sendTime;
        this.context = context;
        this.user = user;
        this.score = score;
        this.size = size;
        this.ispass = ispass;
        this.state = state;
        this.loadCount = loadCount;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getIspass() {
        return ispass;
    }

    public void setIspass(int ispass) {
        this.ispass = ispass;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLoadCount() {
        return loadCount;
    }

    public void setLoadCount(int loadCount) {
        this.loadCount = loadCount;
    }

    @Override
    public String toString() {
        return "File{" +
                "fid=" + fid +
                ", fname='" + fname + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", context='" + context + '\'' +
                ", user=" + user +
                ", score=" + score +
                ", size=" + size +
                ", ispass=" + ispass +
                ", state=" + state +
                ", loadCount=" + loadCount +
                '}';
    }
}
