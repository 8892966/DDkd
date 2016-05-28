package com.example.user.ddkd.Presenter;

import com.example.user.ddkd.Model.IZhuCeAndForgetModel;
import com.example.user.ddkd.Model.ZhuCeAndForgetModelImpl;
import com.example.user.ddkd.View.IForgetView;

/**
 * Created by User on 2016-05-23.
 */
public class ForgetPresenterImpl implements IForgetPresenter,ZhuCeAndForgetModelImpl.ForgetListener,ZhuCeAndForgetModelImpl.phoExistListener {
    private IZhuCeAndForgetModel iZhuCeAndForgetModel;
    private IForgetView iForgetView;
    public ForgetPresenterImpl(IForgetView iForgetView){
        this.iForgetView=iForgetView;
        iZhuCeAndForgetModel=new ZhuCeAndForgetModelImpl();
    }

    @Override
    public void phoExist(String phone) {
        iZhuCeAndForgetModel.phoExist(phone,this);
        iForgetView.yanzhengmabuttonText("检查中...");
    }

    @Override
    public void modifyPsw(String phone) {
        iZhuCeAndForgetModel.modifyPsw(phone,this);
        iForgetView.countDown();
    }

    @Override
    public void UpdatePsw(String phone,String password,String verify) {
        iZhuCeAndForgetModel.UpdatePsw(phone,password,verify,this);
        iForgetView.showProgressDialog();
    }


    @Override
    public void phoExist() {
        iForgetView.yanzhengmabuttonText("验证码");
    }

    @Override
    public void phoisExist() {
        iForgetView.yanzhengmabuttonEnabled(false);
        iForgetView.ToastShow("用户不存在！");
    }

    @Override
    public void phoNotExist() {
        iForgetView.yanzhengmabuttonEnabled(true);

    }

    @Override
    public void phoExistonErrorResponse() {
        iForgetView.yanzhengmabuttonText("验证码");
        iForgetView.yanzhengmabuttonEnabled(false);
        iForgetView.ToastShow("网络连接中断");
    }

    @Override
    public void Error() {
        iForgetView.ToastShow("获取验证码失败");
    }

    @Override
    public void Phone_No_Exists() {
        iForgetView.ToastShow("没有该用户！");
    }

    @Override
    public void Success() {
        iForgetView.ToastShow("请留意您的短信");
    }

    @Override
    public void Out_Of_Range() {
        iForgetView.ToastShow("同一手机号验证码短信发送超出5条");
    }

    @Override
    public void Failure() {
        iForgetView.ToastShow("获取验证码失败");
    }

    @Override
    public void onErrorResponse() {
        iForgetView.ToastShow("网络连接中断");
    }

    @Override
    public void UpdatePswSuccess() {
        iForgetView.closeProgressDialog();
        iForgetView.ToastShow("密码修改成功，请重新登录");
        iForgetView.ExitActivity();
    }

    @Override
    public void verify_ERROR() {
        iForgetView.closeProgressDialog();
        iForgetView.ToastShow("密码修改失败，验证码错误");
    }

    @Override
    public void UpdatePswError() {
        iForgetView.closeProgressDialog();
        iForgetView.ToastShow("密码修改失败，原密码与新密码相同");
    }
}