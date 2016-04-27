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
    private String lstate;
    private String ostate;

    public String getChutime() {
        return chutime;
    }

    public String getLstate() {
        return lstate;
    }

    public String getOstate() {
        return ostate;
    }

    public String getRutime() {
        return rutime;
    }

    public String getShouru() {
        return shouru;
    }

    public String getZhichu() {
        return zhichu;
    }

    public void setChutime(String chutime) {
        this.chutime = chutime;
    }

    public void setLstate(String lstate) {
        this.lstate = lstate;
    }

    public void setOstate(String ostate) {
        this.ostate = ostate;
    }

    public void setRutime(String rutime) {
        this.rutime = rutime;
    }

    public void setShouru(String shouru) {
        this.shouru = shouru;
    }

    public void setZhichu(String zhichu) {
        this.zhichu = zhichu;
    }

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
