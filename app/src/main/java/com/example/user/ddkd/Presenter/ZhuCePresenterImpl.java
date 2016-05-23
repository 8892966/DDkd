package com.example.user.ddkd.Presenter;

import com.example.user.ddkd.Model.IZhuCeAndForgetModel;
import com.example.user.ddkd.Model.ZhuCeAndForgetModelImpl;
import com.example.user.ddkd.View.IZhuCeView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-05-20.
 */
public class ZhuCePresenterImpl implements IZhuCePresenter, ZhuCeAndForgetModelImpl.SSubmitPicturesListener {
    private static ZhuCePresenterImpl zhuCePresenter;
    private IZhuCeView iZhuCeView;
    private IZhuCeAndForgetModel iZhuCeModel;
    private int i=1;
    private ZhuCePresenterImpl(){

    }
    private ZhuCePresenterImpl(IZhuCeView iZhuCeView){
        this.iZhuCeView=iZhuCeView;
        this.iZhuCeModel=new ZhuCeAndForgetModelImpl();
    }

    public static ZhuCePresenterImpl getInstance(IZhuCeView iZhuCeView){
            if (zhuCePresenter == null&&iZhuCeView!=null) {
                synchronized (ZhuCePresenterImpl.class) {
                    if (zhuCePresenter == null) {
                        zhuCePresenter = new ZhuCePresenterImpl(iZhuCeView);
                    }
                }
            }
            return zhuCePresenter;
    }

    @Override
    public void addmap(Map<String, String> map) {
        this.map.putAll(map);
    }

    @Override
    public String getmap(String key) {
        return map.get(key);
    }


    @Override
    public void SubmitPictures(int count,String name,String phone,File file) {
        if(count==4){
            iZhuCeView.showProgressDialog(4);
        }else
        if(count==3){

           }
        iZhuCeModel.SubmitPictures(name,phone,file,this);
      }

    @Override
    public void SubmitData() {
        Map<String,String> maps=new HashMap<>();
        maps.put("class", map.get("class"));
        maps.put("college", map.get("college"));
        maps.put("number", map.get("number"));
        maps.put("password", map.get("password"));
        maps.put("IdCardNum", map.get("IdCardNum"));
        maps.put("phone", map.get("phone"));
        maps.put("sex", map.get("sex"));
        maps.put("shortphone", map.get("shortphone"));
        maps.put("username", map.get("username"));
        iZhuCeModel.SubmitData(maps,this);
    }

    @Override
    public void PhoExist(String s) {
        iZhuCeView.PhoExisting();
        iZhuCeModel.phoExist(s,this);
    }


    @Override
    public void onFailure() {
        iZhuCeView.showToast("提交超时，请重新提交");
    }

    @Override
    public void onException() {
        iZhuCeView.showToast("信息有误!!!");
    }

    @Override
    public void SUCCESS() {
        iZhuCeView.Submit();
    }

    @Override
    public void MAXSIZE_OUT() {
        iZhuCeView.showToast("图片内存过大");
        iZhuCeView.closeProgressDialog();
    }

    @Override
    public void UPLOAD_FILE_FORMAT_ERROR() {
        iZhuCeView.showToast("上传文件格式错误");
        iZhuCeView.closeProgressDialog();
    }

    @Override
    public void UPLOAD_FAIL() {
        iZhuCeView.showToast("上传失败");
        iZhuCeView.closeProgressDialog();
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {
        iZhuCeView.onLoading(total,current);
    }

    @Override
    public void ERROR() {
        iZhuCeView.showToast("提交失败，请重新提交");
        iZhuCeView.closeProgressDialog();
    }

    @Override
    public void DateSUCCESS() {
        iZhuCeView.UploadSUCCESS();
    }

    @Override
    public void DateERROR() {
        iZhuCeView.showToast("提交失败，请重新提交");
        iZhuCeView.closeProgressDialog();
    }

    @Override
    public void ErrorListener() {
        iZhuCeView.showToast("网络异常");
    }

    @Override
    public void phoExist() {
        iZhuCeView.PhoExist();
    }

    @Override
    public void phoisExist() {
        iZhuCeView.PhoisExist();
    }

    @Override
    public void phoNotExist() {
        iZhuCeView.PhoisNotExist();
        iZhuCeView.showToast("用户已存在！");
    }

    @Override
    public void phoExistonErrorResponse() {
        iZhuCeView.PhoisNotExist();
        iZhuCeView.PhoExist();
        iZhuCeView.showToast("网络异常");
    }

}
