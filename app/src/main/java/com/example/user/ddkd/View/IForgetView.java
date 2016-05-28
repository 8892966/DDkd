package com.example.user.ddkd.View;

/**
 * Created by User on 2016-05-23.
 */
public interface IForgetView {
    void yanzhengmabuttonEnabled(boolean b);
    void yanzhengmabuttonText(String s);
    void ToastShow(String s);
    void showProgressDialog();
    void closeProgressDialog();
    void ExitActivity();
    void countDown();
}
