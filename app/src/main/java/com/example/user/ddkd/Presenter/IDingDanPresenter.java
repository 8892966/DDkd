package com.example.user.ddkd.Presenter;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.ddkd.beam.OrderInfo;

/**
 * Created by User on 2016-05-13.
 */
public interface IDingDanPresenter {
    void loadDingDins(int xuanzhe,String token);
    void ChangeDingDins(OrderInfo info,String id, int xuanzhe,int position,String token);
}
