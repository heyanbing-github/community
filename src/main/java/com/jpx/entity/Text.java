package com.jpx.entity;

public class Text {

    private String tid;

    private User user;//楼主

    private String title;//标题

    private String sendTime;//发送时间

    private int  fabulousNum;//点赞数

    private int  commentsNum;//评论数

    private int score;//基础积分

    private  int ispass;//#0审核通过 1审核等待 2审核不通过

    private  int state;//#0显示 1删除

    private  String content;

    public Text() {
    }

    public Text(String tid, User user, String title, String sendTime, int fabulousNum, int commentsNum, int score, int ispass, int state, String content) {
        this.tid = tid;
        this.user = user;
        this.title = title;
        this.sendTime = sendTime;
        this.fabulousNum = fabulousNum;
        this.commentsNum = commentsNum;
        this.score = score;
        this.ispass = ispass;
        this.state = state;
        this.content = content;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getFabulousNum() {
        return fabulousNum;
    }

    public void setFabulousNum(int fabulousNum) {
        this.fabulousNum = fabulousNum;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Text{" +
                "tid='" + tid + '\'' +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", fabulousNum=" + fabulousNum +
                ", commentsNum=" + commentsNum +
                ", score=" + score +
                ", ispass=" + ispass +
                ", state=" + state +
                ", content='" + content + '\'' +
                '}';
    }
}
