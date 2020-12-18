package com.jpx.entity;

/**
 * 用户表
 */
public class User {

    private String uid;

    private String username;

    private String password;

    private String nickName;

    private String phone;

    private String email;

    private String sex;

    private String uphoto;//头像

    private String address;//地址

    private int state;//0存在  1不存在

    public User() {
    }

    public User(String uid, String username, String password, String nickName, String phone, String email, String sex, String uphoto, String address, int state) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.uphoto = uphoto;
        this.address = address;
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUphoto() {
        return uphoto;
    }

    public void setUphoto(String uphoto) {
        this.uphoto = uphoto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                ", uphoto='" + uphoto + '\'' +
                ", address='" + address + '\'' +
                ", state=" + state +
                '}';
    }
}
