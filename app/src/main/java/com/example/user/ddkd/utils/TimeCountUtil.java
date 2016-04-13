package com.example.user.ddkd.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * Created by User on 2016-04-13.
 */
public class TimeCountUtil extends CountDownTimer {
    private TextView textView;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCountUtil(long millisInFuture, long countDownInterval,TextView textView) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.textView=textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setText(millisUntilFinished/1000+"s");
    }

    @Override
    public void onFinish() {

        textView.setVisibility(View.GONE);
    }
}
