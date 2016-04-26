package com.example.user.ddkd.text;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Payment {
    private String name;
    private double money;
    private String Tname;
    private String counter;
    private String time1;
    private String time2;
    private String shouru;
    private String zhichu;
    private String chutime;
    private String rutime;

    public String getCounter() {
        return counter;
    }

    public double getMoney() {
        return money;
    }

    public String getTime1() {
        return time1;
    }

    public String getTname() {
        return Tname;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

    public String getTime2() {
        return time2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }
}
