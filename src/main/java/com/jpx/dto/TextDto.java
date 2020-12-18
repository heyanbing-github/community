package com.jpx.dto;

import com.jpx.entity.Text;

public class TextDto {
    private Text text;
    private String kinds;

    public TextDto() {
    }

    public TextDto(Text text, String kinds) {
        this.text = text;
        this.kinds = kinds;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    @Override
    public String toString() {
        return "TextDto{" +
                "text=" + text +
                ", kinds='" + kinds + '\'' +
                '}';
    }
}
