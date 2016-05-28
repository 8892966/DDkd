package com.example.user.ddkd.Presenter;

import com.example.user.ddkd.Model.ZhuCeAndForgetModelImpl;

/**
 * Created by User on 2016-05-23.
 */
public interface IForgetPresenter {
    void phoExist(String phone);//判断用户是否已注册
    void modifyPsw(String phone);//获取验证码
    void UpdatePsw(String phone,String password,String verify);//修改密码
}
