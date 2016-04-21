package com.example.user.ddkd.beam;

/**
 * Created by User on 2016-04-21.
 */
public class QOrderInfo {
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

    @Override
    public String toString() {
        return "QOrderInfo{" +
                "uid='" + uid + '\'' +
                ", Price='" + Price + '\'' +
                ", tip='" + tip + '\'' +
                ", OrderTime='" + OrderTime + '\'' +
                ", ReceivePlace='" + ReceivePlace + '\'' +
                ", PaymentMethod='" + PaymentMethod + '\'' +
                ", addressee='" + addressee + '\'' +
                ", weight='" + weight + '\'' +
                ", ExpressCompany='" + ExpressCompany + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", Message='" + Message + '\'' +
                '}';
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
}
