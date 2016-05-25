package com.example.user.ddkd.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by User on 2016-04-09.
 */
public class PasswordUtil {

    public static boolean isSame(Context context,String password1,String password2){
        if(!TextUtils.isEmpty(password1)){
            if (password1.length()>=6&&password1.length()<=32) {
                if (password1.equals(password2)) {
                    return true;
                } else {
                    Toast.makeText(context, "确认密码不相同", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(context,"密码不能低于六位或高于32位",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
