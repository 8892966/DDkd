package com.example.user.ddkd.beam;

import java.io.Serializable;

/**
 * Created by User on 2016-04-21.
 */
public class QOrderInfo implements Serializable {
    private String uid;
    private String Price;
    private String tip;
    private String OrderTime;
    private String ReceivePlace;
    private String PaymentMethod;
    private String addressee;
    private String weight;
    private String ExpressCompany;
    private String phone;
    private String username;
    private String Message;
    private String orderid;
    private String bespeak;
    private  int zhuantai=0;

    public String getBespeak() {
        return bespeak;
    }

    public void setBespeak(String bespeak) {
        this.bespeak = bespeak;
    }

    public int getZhuantai() {
        return zhuantai;
    }

    public void setZhuantai(int zhuantai) {
        this.zhuantai = zhuantai;
    }


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getReceivePlace() {
        return ReceivePlace;
    }

    public void setReceivePlace(String receivePlace) {
        ReceivePlace = receivePlace;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getExpressCompany() {
        return ExpressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        ExpressCompany = expressCompany;
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

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QOrderInfo that = (QOrderInfo) o;

        return orderid.equals(that.orderid);

    }

    @Override
    public int hashCode() {
        return orderid.hashCode();
    }
}
