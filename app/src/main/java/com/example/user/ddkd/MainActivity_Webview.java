package com.example.user.ddkd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/4/23.
 */
public class MainActivity_Webview extends Activity implements View.OnClickListener {
    private WebView webView;
    private ImageView exit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aboutdd);
        webView= (WebView) findViewById(R.id.webview);
        String url="https://www.baidu.com/";
        webView.loadUrl(url);
        exit= (ImageView) findViewById(R.id.setExit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.setExit:
                finish();
                break;
        }
    }
}
