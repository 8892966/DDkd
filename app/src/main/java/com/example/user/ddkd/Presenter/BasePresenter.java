package com.example.user.ddkd.Presenter;

import android.app.Activity;
import android.widget.Toast;

import com.example.user.ddkd.Model.DingDanModelImpl;
import com.example.user.ddkd.Model.ITokenManage;
import com.example.user.ddkd.View.IDingDanView;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.NewAutologonUtil;

import java.lang.reflect.Method;

/**
 * Created by User on 2016-05-17.
 */
public abstract class BasePresenter implements ITokenManage{

    private Activity activity;
    public BasePresenter(Activity iDingDanView){
        activity= iDingDanView;
    }

    @Override
    public void tokenouttime(final String ClassName, final String method,String url, final String ListenerS, final ITokenManage Listener) {
        NewAutologonUtil.getToken(activity, new NewAutologonUtil.OnLogoListent() {
            @Override
            public void Success() {
                try {
                    Class c = Class.forName(ClassName);
                    Object obj=c.newInstance();
                    Method declaredMethod = c.getDeclaredMethod(method, String.class,Class.forName(ListenerS));
                    declaredMethod.invoke(obj,"url",Listener);
                } catch (Exception e) {
                    e.printStackTrace();
                    Error();
                }
            }

            @Override
            public void Error() {
                try {
                    Toast.makeText(activity, "网络连接出错,请重新登陆", Toast.LENGTH_SHORT).show();
                    Exit.exit(activity);
                } catch (Exception e) {
                    e.printStackTrace();
                    Error();
                }
            }
        });
    }



    @Override
    public void yidiensdfsdf() {
        Exit.exit(activity);
        Toast.makeText(activity, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
    }
}
