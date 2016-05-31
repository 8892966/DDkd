package com.example.user.ddkd.Model;

import java.io.File;
import java.util.Map;

/**
 * Created by User on 2016-05-20.
 */
public interface IZhuCeAndForgetModel {
    //提交图片
    void SubmitPictures(String name, String phone, File file, ZhuCeAndForgetModelImpl.SSubmitPicturesListener sSubmitPicturesListener);
    //提交数据
    void SubmitData(Map<String, String> map, ZhuCeAndForgetModelImpl.SSubmitPicturesListener sSubmitPicturesListener);
    //检查电话是否存在
    void phoExist(String phone,ZhuCeAndForgetModelImpl.phoExistListener sSubmitPicturesListener);//判断用户是否已注册
    //获取验证码
    void modifyPsw(String phone,ZhuCeAndForgetModelImpl.ForgetListener forgetListener);//获取验证码
    //修改密码
    void UpdatePsw(String phone,String password,String verify,ZhuCeAndForgetModelImpl.ForgetListener forgetListener);
}
