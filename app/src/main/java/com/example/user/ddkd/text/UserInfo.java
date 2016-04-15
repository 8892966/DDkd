package com.example.user.ddkd.text;

/**
 * Created by Administrator on 2016/4/13.
 */
public class UserInfo {
    private String username;
    private int shortphone;
    private long phone;
    private String college;
    private int number;
    private String level;

    public int getNumber() {
        return number;
    }

    public int getShortphone() {
        return shortphone;
    }

    public long getPhone() {
        return phone;
    }

    public String getCollege() {
        return college;
    }

    public String getLevel() {
        return level;
    }

    public String getUsername() {
        return username;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setShortphone(int shortphone) {
        this.shortphone = shortphone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
