package com.example.user.ddkd.text;

/**
 * Created by Administrator on 2016/4/6.
 */
public class DetailsInfo {
    private int detailsid;
    private String detailsMoney;
    private String username;
    private String courier;//快递公司
    private int telphone;
    private String addr;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getTelphone() {
        return telphone;
    }

    public void setTelphone(int telphone) {
        this.telphone = telphone;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getCourier() {
        return courier;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDetailsid() {
        return detailsid;
    }

    public void setDetailsid(int detailsid) {
        this.detailsid = detailsid;
    }

    public void setDetailsMoney(String detailsMoney) {
        this.detailsMoney = detailsMoney;
    }

    public String getDetailsMoney() {
        return detailsMoney;
    }

}
