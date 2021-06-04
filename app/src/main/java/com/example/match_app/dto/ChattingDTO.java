package com.example.match_app.dto;

import java.io.Serializable;

public class ChattingDTO implements Serializable {
    private String msg;
    private String nickname;
    private String date;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
