package com.example.user.ddkd.Model;

import java.io.File;
import java.util.Map;

/**
 * Created by User on 2016-05-20.
 */
public interface IZhuCeAndForgetModel {
    void SubmitPictures(String name, String phone, File file, ZhuCeAndForgetModelImpl.SSubmitPicturesListener sSubmitPicturesListener);
    void SubmitData(Map<String, String> map, ZhuCeAndForgetModelImpl.SSubmitPicturesListener sSubmitPicturesListener);
    void phoExist(String phone,ZhuCeAndForgetModelImpl.SSubmitPicturesListener sSubmitPicturesListener);//判断用户是否已注册
    void modifyPsw(final ZhuCeAndForgetModelImpl.ForgetListener forgetListener,String phone);//获取验证码
}
