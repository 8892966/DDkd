package com.example.user.ddkd.text;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Payment {
    private String id;
    private double money;
    private String Tname;
    private String time;
    private String flag;
    private String status;
    private String counter;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getFlag() {
        return flag;
    }

    public String getCounter() {
        return counter;
    }

    public double getMoney() {
        return money;
    }

    public String getTime() {
        return time;
    }

    public String getTname() {
        return Tname;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

}
