package com.example.user.ddkd.View;

import com.example.user.ddkd.beam.MainMsgInfo;
import com.example.user.ddkd.beam.QOrderInfo;

import java.util.List;

/**
 * Created by User on 2016-05-26.
 */
public interface IJieDanView {
    void showToast(String content);
    void setMainMsgInfo(MainMsgInfo info);
    void getGDorder();
    void setGDListInfo(List<QOrderInfo> list);
    void setEndListAndItemViewState(int state,int position);
    void setStartListAndItemViewState(int position);
}
