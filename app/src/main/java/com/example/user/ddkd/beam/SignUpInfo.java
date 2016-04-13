package com.example.user.ddkd.beam;

import java.io.Serializable;

/**
 * Created by User on 2016-04-09.
 */
public class SignUpInfo implements Serializable {
    private String phone;//手机长号
    private String password;//密码
    private String username;//用户名
    private String college;//学院
    private String number;//学号
    private String sex;//性别
    private String clazz;//班级
    private String id_card;//身份证
    private String shortnumber;//短号

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getShortnumber() {
        return shortnumber;
    }

    public void setShortnumber(String shortnumber) {
        this.shortnumber = shortnumber;
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
