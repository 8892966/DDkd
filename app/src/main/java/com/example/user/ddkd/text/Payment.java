package com.example.user.ddkd.text;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Payment {
    private String name;
    private double money;
    private String Tname;
    private String counter;
    private long time1;
    private long time2;

    public String getCounter() {
        return counter;
    }

    public double getMoney() {
        return money;
    }

    public long getTime1() {
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

    public void setTime1(long time1) {
        this.time1 = time1;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

    public long getTime2() {
        return time2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime2(long time2) {
        this.time2 = time2;
    }
}
