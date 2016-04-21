package com.example.user.ddkd.beam;

/**
 * Created by User on 2016-04-21.
 */
public class MainMsgInfo {

    private String ystOrder;//昨天订单
    private String todOrder;//今天订单
    private String totalOrder;//总订单
    private String ystTurnover;//昨天营业额
    private String evaluate;//星星数据

    public String getYstOrder() {
        return ystOrder;
    }

    public void setYstOrder(String ystOrder) {
        this.ystOrder = ystOrder;
    }

    public String getTodOrder() {
        return todOrder;
    }

    public void setTodOrder(String todOrder) {
        this.todOrder = todOrder;
    }

    public String getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getYstTurnover() {
        return ystTurnover;
    }

    public void setYstTurnover(String ystTurnover) {
        this.ystTurnover = ystTurnover;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }
}
