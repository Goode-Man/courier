package com.example.machenike.courier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.machenike.http.HttpConfig;

public class SelectExpressActivitys extends AppCompatActivity {

    WebView select_express_webview;
    TextView select_express_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_express);
        select_express_back= (TextView) findViewById(R.id.select_express_back);
        select_express_webview= (WebView) findViewById(R.id.select_express_webview);
        select_express_webview.getSettings().setJavaScriptEnabled(true);
        //WebView加载web资源
        select_express_webview.loadUrl(HttpConfig.kuaidi100);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        select_express_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        select_express_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
