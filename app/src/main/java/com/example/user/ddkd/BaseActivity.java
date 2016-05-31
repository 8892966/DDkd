package com.example.user.ddkd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.baidu.mobstat.StatService;

/**
 * Created by User on 2016-05-18.
 */
public class BaseActivity extends AppCompatActivity {

    protected boolean addStack(){
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        addStacks();
    }

    private void addStacks() {
        if(addStack()) {
            ExitApplication.getInstance().addActivity(this);
        }
    }

    @Override
        protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
