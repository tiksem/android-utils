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
    private static final String HTML = "html";

    private WebView webView;

    public static void loadUrl(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    public static void loadHtml(Context context, String html) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(HTML, html);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        if (intent.hasExtra(URL)) {
            webView.loadUrl(intent.getStringExtra(URL));
        } else {
            webView.loadData(intent.getStringExtra(HTML), "text/html", "UTF-8");
        }
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
