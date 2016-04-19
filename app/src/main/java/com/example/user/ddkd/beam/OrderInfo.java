package com.example.user.ddkd.beam;

/**
 * Created by User on 2016-04-02.
 */
public class OrderInfo {
    //返回状态码
    private String return_code;
    //返回信息
    private String return_msg;
    //订单id
    private String id;
    //订单金额
    private String Price;
    //下单人姓名
    private String username;
    //收件人手机号码
    private String phone;
    //快递公司
    private String ExpressCompany;
    //快递重量
    private String weight;
    //客户收货地址
    private String ReceivePlace;
    //订单评价
    private String Message;
    //客户留言
    private String evaluate;
    //下单时间
    private String time;
    //
    private String addressee;

    private int state=0;//判断是否有操作，如果什么都没有做就为0，如果点击了完成就设在为1，点击了退单，设置为2，点击了完成，设置为3

//    @Override
//    public String toString() {
//        return "OrderInfo{" +
//                "return_code='" + return_code + '\'' +
//                ", return_msg='" + return_msg + '\'' +
//                ", id='" + id + '\'' +
//                ", Price='" + Price + '\'' +
//                ", username='" + username + '\'' +
//                ", phone='" + phone + '\'' +
//                ", ExpressCompany='" + ExpressCompany + '\'' +
//                ", weight='" + weight + '\'' +
//                ", ReceivePlace='" + ReceivePlace + '\'' +
//                ", Message='" + Message + '\'' +
//                ", evaluate='" + evaluate + '\'' +
//                ", time=" + time +
//                ", addressee='" + addressee + '\'' +
//                '}';
//    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExpressCompany() {
        return ExpressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        ExpressCompany = expressCompany;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getReceivePlace() {
        return ReceivePlace;
    }

    public void setReceivePlace(String receivePlace) {
        ReceivePlace = receivePlace;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }
}
