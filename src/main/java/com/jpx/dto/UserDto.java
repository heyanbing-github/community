package com.jpx.dto;

import com.jpx.entity.User;

/**
 * 封装用户数据，积分信息
 */
public class UserDto {

    private User user;
    private int score;

    public UserDto(User user, int score) {
        this.user = user;
        this.score = score;
    }

    public UserDto() {
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
}
