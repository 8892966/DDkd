package com.example.user.ddkd.Presenter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-05-20.
 */
public interface IZhuCePresenter {
    Map<String,String> map=new HashMap<>();
    void addmap(Map<String, String> map);
    String getmap(String key);
    void SubmitPictures(int count, String name, String phone, File file);
    void SubmitData();
    void PhoExist(String s);
}
