package com.example.user.ddkd.Presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.user.ddkd.Model.IJieDanModel;
import com.example.user.ddkd.Model.JieDanModelImpl;
import com.example.user.ddkd.View.IJieDanView;
import com.example.user.ddkd.beam.MainMsgInfo;
import com.example.user.ddkd.beam.QOrderInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2016-05-26.
 */
public class JieDanPresenterImpl extends BasePresenter implements IJieDanPresenter,JieDanModelImpl.JieDanListener {
    private IJieDanModel iJieDanModel;
    private IJieDanView iJieDanView;
    private Map<String,Integer> map;

    public JieDanPresenterImpl(@NonNull Activity iJieDanView) {
        super(iJieDanView);
        map=new HashMap<>();
        iJieDanModel=new JieDanModelImpl();
        this.iJieDanView= (IJieDanView) iJieDanView;
    }

    @Override
    public void CountOrder(String token) {
        String url = "http://www.louxiago.com/wc/ddkdtest/admin.php/Order/CountOrder/token/" + token;
        iJieDanModel.CountOrder(url,this);
    }

    @Override
    public void RobOrder(int position,String XGtoken,String token,String id) {
        if(iJieDanView!=null) {
            iJieDanView.setStartListAndItemViewState(position);
        }
        String url = "http://www.louxiago.com/wc/ddkdtest/admin.php/Order/RobOrder/orderId/" + id + "/token/" + token + "/deviceId/" + XGtoken;
        iJieDanModel.RobOrder(url,this);
        map.put(url, position);
    }

    @Override
    public void getBespeakOrder(String token) {
        String url = "http://www.louxiago.com/wc/ddkdtest/admin.php/Order/getBespeakOrder/token/" + token;
        iJieDanModel.getBespeakOrder(url, this);
    }

    @Override
    public void RobBespeakOrder(int position,String token,String id) {
        String url = "http://www.louxiago.com/wc/ddkdtest/admin.php/Order/RobBespeakOrder/orderId/"+id+"/token/" + token;
        iJieDanModel.RobBespeakOrder(url, this);
    }

    @Override
    public void RemoveView() {
        iJieDanView=null;
    }

    @Override
    public void GD_ROB_SUCCESS(String url) {
        iJieDanView.showToast("抢单成功");
    }

    @Override
    public void GD_ROB_FAIL() {
        if(iJieDanView!=null){
            iJieDanView.showToast("抢单不成功");
        }
    }

    @Override
    public void GD_ROB_ERROR() {
        if(iJieDanView!=null) {
            iJieDanView.showToast("操作失败");
        }
    }

    @Override
    public void Exception() {
        if(iJieDanView!=null) {
            iJieDanView.showToast("信息有误!!!");
        }
    }

    @Override
    public void onErrorResponse() {
        if(iJieDanView!=null) {
            iJieDanView.showToast("网络中断");
        }
    }

    @Override
    public void GD_MSG_NODATA() {
    }

    @Override
    public void GD_MSG_SUCCESS(List<QOrderInfo> list) {
        if(iJieDanView!=null) {
            iJieDanView.setGDListInfo(list);
        }
    }

    @Override
    public void ROB_SUCCESS(String url) {
        int position=map.get(url);
        map.remove(url);
        if(iJieDanView!=null) {
            iJieDanView.setEndListAndItemViewState(2, position);
        }
    }

    @Override
    public void ROB_SUCCESS2(String url) {
        int position=map.get(url);
        map.remove(url);
        if(iJieDanView!=null) {
            iJieDanView.setEndListAndItemViewState(1, position);
        }
//        iJieDanView.showToast("抢单成功");
    }

    @Override
    public void ROB_ERROR(String url) {
        int position=map.get(url);
        map.remove(url);
        if(iJieDanView!=null) {
            iJieDanView.setEndListAndItemViewState(0, position);
        }
    }

    @Override
    public void ROB_ERROR2(String url) {
        int position=map.get(url);
        map.remove(url);
        if(iJieDanView!=null) {
            iJieDanView.setEndListAndItemViewState(3, position);
        }
//        iJieDanView.showToast("当前的单已被抢");
    }

    @Override
    public void CountOrder_SUCCESS(MainMsgInfo info) {
        if(iJieDanView!=null) {
            iJieDanView.setMainMsgInfo(info);
        }
    }

    @Override
    public void CountOrder_NEXT() {
        if(iJieDanView!=null) {
            iJieDanView.getGDorder();
        }
    }

}
