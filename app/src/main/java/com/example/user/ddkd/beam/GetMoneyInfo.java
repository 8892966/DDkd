package com.example.user.ddkd.beam;

/**
 * Created by Administrator on 2016/4/14.
 */
public class GetMoneyInfo {
    private int uid;
    private double getmoney;
    private String Tname;
    private String counter;
    private String name;
    private long Time1;
    private long Time2;

    public void setId(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return uid;
    }

    public double getGetmoney() {
        return getmoney;
    }

    public void setGetmoney(double getmoney) {
        this.getmoney = getmoney;
    }

    public long getTime1() {
        return Time1;
    }

    public void setTime1(long time1) {
        Time1 = time1;
    }

    public long getTime2() {
        return Time2;
    }

    public void setTime2(long time2) {
        Time2 = time2;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getTname() {
        return Tname;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
