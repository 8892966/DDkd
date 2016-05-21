package com.example.user.ddkd.Model;

import java.io.File;
import java.util.Map;

/**
 * Created by User on 2016-05-20.
 */
public interface IZhuCeModel {
    void SubmitPictures(String name, String phone, File file, ZhuCeModelImpl.SSubmitPicturesListener sSubmitPicturesListener);
    void SubmitData(Map<String, String> map, ZhuCeModelImpl.SSubmitPicturesListener sSubmitPicturesListener);
    void phoExist(String phone,ZhuCeModelImpl.SSubmitPicturesListener sSubmitPicturesListener);//判断用户是否已注册
}
