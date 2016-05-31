package com.example.user.ddkd.Model;

/**
 * Created by User on 2016-05-26.
 */
public interface IJieDanModel {
    void CountOrder(String url,JieDanModelImpl.JieDanListener jieDanListener);//页面信息
    void RobOrder(String url,JieDanModelImpl.JieDanListener jieDanListener);//抢单
    void getBespeakOrder(String url,JieDanModelImpl.JieDanListener jieDanListener);//挂单信息
    void RobBespeakOrder(String url,JieDanModelImpl.JieDanListener jieDanListener);//抢单挂单
}
