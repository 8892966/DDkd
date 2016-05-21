package com.example.user.ddkd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.Presenter.ZhuCePresenterImpl;
import com.example.user.ddkd.utils.SmsUtils;
import com.example.user.ddkd.utils.YanZhenMaUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-04-19.
 */
public class ZhuCe1Fragment extends Fragment implements View.OnClickListener {
    private EditText et_phone_number;
    private EditText et_yanzhengma;
    private CheckBox cb_xieyi;
    private TextView tv_button_yanzhengma;
    private TextView tv_button_yuedu;
    private YanZhenMaUtil yanZhenMaUtil;
    private SmsUtils smsUtils;
    private ZhuCePresenterImpl zhuCePresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhuce1, container, false);
        zhuCePresenter=ZhuCePresenterImpl.getInstance(null);
        yanZhenMaUtil = new YanZhenMaUtil();//初始化验证码工具类
        smsUtils = new SmsUtils();
        smsUtils.startGetSms(getActivity());
        et_phone_number = (EditText) view.findViewById(R.id.et_phone_number);//手机号
        et_yanzhengma = (EditText) view.findViewById(R.id.et_yanzhengma);//验证码
        cb_xieyi = (CheckBox) view.findViewById(R.id.cb_xieyi);//协议
        tv_button_yuedu = (TextView) view.findViewById(R.id.tv_button_yuedu);//阅读协议
        tv_button_yanzhengma = (TextView) view.findViewById(R.id.tv_button_yanzhengma);//获取验证码
        tv_button_yuedu.setOnClickListener(this);
        tv_button_yanzhengma.setOnClickListener(this);
        tv_button_yanzhengma.setEnabled(false);//初始化获取验证码按钮状态
        cb_xieyi.setOnClickListener(this);
        //只有输入手机号码时才能点击获取验证码
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (et_phone_number.length() == 11) {
                    zhuCePresenter.PhoExist(et_phone_number.getText().toString());
                } else {
                    tv_button_yanzhengma.setEnabled(false);
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_button_yanzhengma:
                yanZhenMaUtil.sendYZM(getActivity(), et_phone_number, tv_button_yanzhengma);
                break;
            case R.id.tv_button_yuedu:
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("title", "DD快递服务协议");
                intent.putExtra("url", "http://www.louxiago.com/wc/ddkd/index.php/Agreement/index.html");
                startActivity(intent);
                break;
            case R.id.cb_xieyi:
                ZhuCeActivity zhuCeActivity= (ZhuCeActivity) getActivity();
                if (cb_xieyi.isChecked()) {
                        zhuCeActivity.Agreed();
                } else {
                        zhuCeActivity.Against();
                }
                break;
        }
    }

    public boolean next(){
        if (yanZhenMaUtil.isYZM(getActivity(), et_yanzhengma, et_phone_number)) {
            //注册信息
        Map<String,String> map=new HashMap<>();
        map.put("phone", et_phone_number.getText().toString().trim());
        zhuCePresenter.addmap(map);
        return true;
            }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    public void yanzhengsettext(String s){
        tv_button_yanzhengma.setText(s);
    }
    public void yanzhengsetEnabled(boolean b){
        tv_button_yanzhengma.setEnabled(b);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        smsUtils.cloesGetSms(getActivity());
    }

}
