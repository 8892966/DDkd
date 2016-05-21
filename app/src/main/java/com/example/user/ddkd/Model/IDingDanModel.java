package com.example.user.ddkd.Model;

/**
 * Created by User on 2016-05-13.
 */
public interface IDingDanModel {
    void loadDingDins(String url, DingDanModelImpl.OnloadDingDinsListListener listener);
    void ChangeDingDins(String url, DingDanModelImpl.OnChangeDingDinsListListener listener);
}
