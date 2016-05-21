package com.example.user.ddkd.Presenter;

import android.support.annotation.NonNull;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.user.ddkd.Model.DingDanModelImpl;
import com.example.user.ddkd.Model.IDingDanModel;
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
    private Map<String,ProgressBar> pb_button;
    private Map<String,TextView> button;
    private Map<String,OrderInfo> info;
    private Map<String,Integer> xuanzhe;
    public DingDanPresenterImpl(IDingDanView iDingDanView){
        super(iDingDanView);
        iDingDinModel=new DingDanModelImpl();
        this.iDingDanView=iDingDanView;
        pb_button=new HashMap<>();
        button=new HashMap<>();
        info=new HashMap<>();
        xuanzhe=new HashMap<>();
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
    public void ChangeDingDins(OrderInfo info,int xuanzhe,ProgressBar pb_button, TextView button,String token) {
        String url = getChangeURL(info.getId(),xuanzhe,token);
        iDingDinModel.ChangeDingDins(url,this);
        iDingDanView.showChangeProgress(pb_button,button);
        this.xuanzhe.put(url,xuanzhe);
        this.info.put(url,info);
        this.pb_button.put(url,pb_button);
        this.button.put(url,button);
    }

    private String getChangeURL(String id,int State,String token) {
        return "http://www.louxiago.com/wc/ddkd/admin.php/Order/setOrderState/id/" + id + "/state/" + State + "/token/"+ token;
    }

    @NonNull
    private String getloadURL(int xuanzhe,String token) {
        return "http://www.louxiago.com/wc/ddkd/admin.php/Order/getOrder/state/" + xuanzhe + "/token/"+token ;
    }

    @Override
    public void onChangeSuccess(String url) {
        iDingDanView.hideChangeProgress(this.pb_button.get(url), this.button.get(url));
        iDingDanView.removeDindDan(this.info.get(url),this.xuanzhe.get(url));
    }

    @Override
    public void onChangeError(String url) {
        iDingDanView.hideChangeProgress(this.pb_button.get(url), this.button.get(url));
        iDingDanView.showErrorToast();
    }

    @Override
    public void onChangeFailure(String msg, Exception e,String url) {
        iDingDanView.hideChangeProgress(this.pb_button.get(url), this.button.get(url));
        iDingDanView.onChangeFailure(e);
    }

    @Override
    public void onChangeErrorResponse(VolleyError volleyError,String url) {
        iDingDanView.hideChangeProgress(this.pb_button.get(url), this.button.get(url));
        iDingDanView.onChangeErrorResponse(xuanzhe.get(url), pb_button.get(url), button.get(url));
    }

    @Override
    public void onloadSuccess(List<OrderInfo> list,String url) {
        iDingDanView.hideProgress(this.xuanzhe.get(url));
        iDingDanView.resetDindDan(list,this.xuanzhe.get(url));
    }

    @Override
    public void onloadFailure(String msg, Exception e,String url) {
        iDingDanView.hideProgress(this.xuanzhe.get(url));
        iDingDanView.onloadErrorResponse(this.xuanzhe.get(url));
        iDingDanView.onChangeFailure(e);
    }

    @Override
    public void onloadErrorResponse(VolleyError volleyError,String url) {
        iDingDanView.hideProgress(this.xuanzhe.get(url));
        iDingDanView.onloadErrorResponse(this.xuanzhe.get(url));
    }

}
