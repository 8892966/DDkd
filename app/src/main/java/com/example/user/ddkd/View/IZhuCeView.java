package com.example.user.ddkd.View;

/**
 * Created by User on 2016-05-20.
 */
public interface IZhuCeView {
    void showToast(String content);
    void showProgressDialog(int max);
    void closeProgressDialog();
    void onLoading(long total, long current);
    void Submit();
    void UploadSUCCESS();
    void PhoExisting();
     void PhoExist();
     void PhoisExist();
     void PhoisNotExist();
}
