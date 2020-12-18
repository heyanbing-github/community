package com.jpx.entity;

/**
 * 公告类
 */
public class Notice {
    private String nid;

    private Admin admin;//管理员

    private String sendTime;

    private String content;//公告内容

    public Notice(String nid, Admin admin, String sendTime, String content) {
        this.nid = nid;
        this.admin = admin;
        this.sendTime = sendTime;
        this.content = content;
    }

    public Notice() {
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "nid='" + nid + '\'' +
                ", admin=" + admin +
                ", sendTime='" + sendTime + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
