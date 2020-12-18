
package com.jpx.dto;
import java.io.Serializable;
public class Result<T> implements Serializable {
    private int state;//表示是否成功

    private int whichOne;//管理员登录[0]还是用户登录[1]
    public static final int SUCCESS=0;//成功
    public static final int ERROR=1;//失败
    public static final int NOT_LOGIN=2;//未登录

    private String msg;//消息

    private T data;//数据


    public Result() {
    }

    public Result(int state, int whichOne, String msg, T data) {
        this.state = state;
        this.whichOne = whichOne;
        this.msg = msg;
        this.data = data;
    }

    public int getWhichOne() {
        return whichOne;
    }

    public void setWhichOne(int whichOne) {
        this.whichOne = whichOne;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

