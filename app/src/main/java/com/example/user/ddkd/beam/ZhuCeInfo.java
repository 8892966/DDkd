package com.example.user.ddkd.beam;

import java.io.Serializable;

/**
 * Created by User on 2016-04-09.
 */
public class ZhuCeInfo implements Serializable {
    private String phone;
    private String password;
    private String username;
    private String college;
    private String number;

    @Override
    public String toString() {
        return "ZhuCeInfo{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", college='" + college + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
