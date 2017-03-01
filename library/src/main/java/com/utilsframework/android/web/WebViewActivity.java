package com.utilsframework.android.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.utilsframework.android.R;

/**
 * Created by CM on 6/17/2015.
 */
public class WebViewActivity extends AppCompatActivity {
    private static final String URL = "url";
    private static final String HTML = "html";

    private WebView webView;
    private View loading;

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

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webView.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
            setTitle(view.getTitle());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        webView = (WebView) findViewById(R.id.webView);
        loading = findViewById(R.id.loading);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        Intent intent = getIntent();
        onCreateLoadData(intent);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreateLoadData(Intent intent) {
        if (intent.hasExtra(URL)) {
            webView.loadUrl(intent.getStringExtra(URL));
        } else {
            webView.loadData(intent.getStringExtra(HTML), "text/html", "UTF-8");
        }
    }

    public WebView getWebView() {
        return webView;
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
