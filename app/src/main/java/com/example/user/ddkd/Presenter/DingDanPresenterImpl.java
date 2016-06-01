package com.example.user.ddkd.Presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.user.ddkd.Model.DingDanModelImpl;
import com.example.user.ddkd.Model.IDingDanModel;
import com.example.user.ddkd.MyApplication;
import com.example.user.ddkd.View.IDingDanView;
import com.example.user.ddkd.beam.OrderInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2016-05-13.
 */
public class DingDanPresenterImpl extends BasePresenter implements IDingDanPresenter,DingDanModelImpl.OnloadDingDinsListListener,DingDanModelImpl.OnChangeDingDinsListListener {
    private IDingDanModel iDingDinModel;
    private IDingDanView iDingDanView;
    private Map<String,Integer> xuanzhe;
    private Map<String,Integer> position;
    private Map<String,String> id;

    public DingDanPresenterImpl(IDingDanView iDingDanView){
        super((Activity) iDingDanView);
        iDingDinModel=new DingDanModelImpl();
        this.iDingDanView=iDingDanView;
        xuanzhe=new HashMap<>();
        position=new HashMap<>();
        id=new HashMap<>();
    }

    @Override
    public void loadDingDins(int xuanzhe,String token) {
        String url = getloadURL(xuanzhe,token);
        iDingDanView.ClearData();
        iDingDanView.showProgress(xuanzhe);
        iDingDinModel.loadDingDins(url, this);
        this.xuanzhe.put(url, xuanzhe);
    }

    @Override
    public void ChangeDingDins(OrderInfo info,String id, int xuanzhe,int position,String token) {
        String url = getChangeURL(info.getId(),xuanzhe,token);
        iDingDinModel.ChangeDingDins(url, this);
        iDingDanView.showChangeProgress(xuanzhe, position);
        this.xuanzhe.put(url, xuanzhe);
        this.position.put(url,position);
        this.id.put(url,id);
    }

    private String getChangeURL(String id,int State,String token) {
        return MyApplication.url+"Order/setOrderState/id/" + id + "/state/" + State + "/token/"+ token;
    }

    @NonNull
    private String getloadURL(int xuanzhe,String token) {
        return MyApplication.url+"Order/getOrder/state/" + xuanzhe + "/token/"+token ;
    }

    @Override
    public void onChangeSuccess(String url) {
        iDingDanView.hideChangeProgress(xuanzhe.get(url), position.get(url));
        iDingDanView.removeDindDan(id.get(url), position.get(url), this.xuanzhe.get(url));
        this.xuanzhe.remove(url);
        this.position.remove(url);
        this.id.remove(url);
    }

    @Override
    public void onChangeError(String url) {
        iDingDanView.hideChangeProgress(xuanzhe.get(url),position.get(url));
        iDingDanView.showErrorToast();
        this.xuanzhe.remove(url);
        this.position.remove(url);
        this.id.remove(url);
    }

    @Override
    public void onChangeFailure(String msg, Exception e,String url) {
        iDingDanView.hideChangeProgress(xuanzhe.get(url),position.get(url));
        iDingDanView.onChangeFailure(e);
        this.xuanzhe.remove(url);
        this.position.remove(url);
        this.id.remove(url);
    }

    @Override
    public void onChangeErrorResponse(VolleyError volleyError,String url) {
        iDingDanView.hideChangeProgress(xuanzhe.get(url),position.get(url));
        iDingDanView.onChangeErrorResponse(xuanzhe.get(url), position.get(url));
        this.xuanzhe.remove(url);
        this.position.remove(url);
        this.id.remove(url);
    }

    @Override
    public void onloadSuccess(List<OrderInfo> list,String url) {
        iDingDanView.hideProgress(this.xuanzhe.get(url));
        iDingDanView.resetDindDan(list, this.xuanzhe.get(url));
        this.xuanzhe.remove(url);
        this.position.remove(url);
        this.id.remove(url);
    }

    @Override
    public void onloadFailure(String msg, Exception e,String url) {
        iDingDanView.hideProgress(this.xuanzhe.get(url));
        iDingDanView.onloadErrorResponse(this.xuanzhe.get(url));
        iDingDanView.onChangeFailure(e);
        this.xuanzhe.remove(url);
        this.position.remove(url);
        this.id.remove(url);
    }

    @Override
    public void onloadErrorResponse(VolleyError volleyError,String url) {
        iDingDanView.hideProgress(this.xuanzhe.get(url));
        iDingDanView.onloadErrorResponse(this.xuanzhe.get(url));
        this.xuanzhe.remove(url);
        this.position.remove(url);
        this.id.remove(url);
    }

}
