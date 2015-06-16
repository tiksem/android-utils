package com.utilsframework.android.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by CM on 6/17/2015.
 */
public class WebViewActivity extends Activity {
    private static final String URL = "url";

    private WebView webView;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra(URL));
        setContentView(webView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!webView.canGoBack()) {
            super.onBackPressed();
        } else {
            webView.goBack();
        }
    }
}
