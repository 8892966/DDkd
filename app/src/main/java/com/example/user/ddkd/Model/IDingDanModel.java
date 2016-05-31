package com.example.user.ddkd.Model;

/**
 * Created by User on 2016-05-13.
 */
public interface IDingDanModel {
    void loadDingDins(String url, DingDanModelImpl.OnloadDingDinsListListener listener);//加载订单
    void ChangeDingDins(String url, DingDanModelImpl.OnChangeDingDinsListListener listener);//改变订单
}
