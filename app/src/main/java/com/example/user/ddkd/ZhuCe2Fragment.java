package com.example.user.ddkd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.user.ddkd.Presenter.ZhuCePresenterImpl;
import com.example.user.ddkd.utils.PasswordUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-04-19.
 */
public class ZhuCe2Fragment extends Fragment {

    private EditText et_password;
    private EditText et_password2;
    private ZhuCePresenterImpl zhuCePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhuce2, container, false);
        zhuCePresenter=ZhuCePresenterImpl.getInstance(null);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_password2 = (EditText) view.findViewById(R.id.et_password2);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean next(){
        String password1 = et_password.getText().toString();
        String password2 = et_password2.getText().toString();
        if (PasswordUtil.isSame(getActivity(), password1, password2)) {
            Map<String,String> map=new HashMap<>();
            map.put("password", password1.trim());
            zhuCePresenter.addmap(map);
            return true;
        }
        return false;
    }

}
