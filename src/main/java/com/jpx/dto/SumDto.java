package com.jpx.dto;

import com.jpx.entity.User;

public class SumDto {

    private User user;
    private int sumFabulous;

    public SumDto() {
    }

    public SumDto(User user, int sumFabulous) {
        this.user = user;
        this.sumFabulous = sumFabulous;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSumFabulous() {
        return sumFabulous;
    }

    public void setSumFabulous(int sumFabulous) {
        this.sumFabulous = sumFabulous;
    }

    @Override
    public String toString() {
        return "SumDto{" +
                "user=" + user +
                ", sumFabulous=" + sumFabulous +
                '}';
    }
}

