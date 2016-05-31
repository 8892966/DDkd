package com.example.user.ddkd.Presenter;

import android.widget.Button;
import android.widget.TextView;

import com.example.user.ddkd.beam.QOrderInfo;

/**
 * Created by User on 2016-05-26.
 */
public interface IJieDanPresenter {
    void CountOrder(String token);//页面信息
    void RobOrder(int position,String XGtoken,String token,String id);//抢单
    void getBespeakOrder(String token);//挂单信息
    void RobBespeakOrder(int position,String token,String id);//抢单挂单
    void RemoveView();//清理引用
}
