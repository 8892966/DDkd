package com.example.user.ddkd.text;

/**
 * Created by Administrator on 2016/4/6.
 */
public class DetailsInfo {
    private long id;
    private double Price;
    private String addressee;
    private String username;
    private long phone;
    private String ExpressCompany;
    private String evaluate;
    private long time;
    private String ReceiverPlace;

    public double getPrice() {
        return Price;
    }

    public long getId() {
        return id;
    }

    public long getPhone() {
        return phone;
    }

    public long getTime() {
        return time;
    }

    public String getAddressee() {
        return addressee;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public String getExpressCompany() {
        return ExpressCompany;
    }

    public String getReceiverPlace() {
        return ReceiverPlace;
    }

    public String getUsername() {
        return username;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public void setExpressCompany(String expressCompany) {
        ExpressCompany = expressCompany;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setReceiverPlace(String receiverPlace) {
        ReceiverPlace = receiverPlace;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
