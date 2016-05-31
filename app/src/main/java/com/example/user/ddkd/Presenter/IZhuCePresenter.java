package com.example.user.ddkd.Presenter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-05-20.
 */
public interface IZhuCePresenter {
    Map<String,String> map=new HashMap<>();
    void addmap(Map<String, String> map);//把信息添加到map
    String getmap(String key);
    void SubmitPictures(int count, String name, String phone, File file);//提交图片
    void SubmitData();//提交数据
    void PhoExist(String s);//坚持账户是否已注册
}
