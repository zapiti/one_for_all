package com.dev.nathan.testtcc.controler;

import android.annotation.SuppressLint;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


import com.dev.nathan.testtcc.R;



import java.util.ArrayList;
import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private LinearLayout load;



    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);


        load = findViewById(R.id.avi2contentWeb);
        mWebView = findViewById(R.id.codeitbro_web_view);

        load.setVisibility(View.VISIBLE);
        Toolbar webViewToolbar = findViewById(R.id.web_view_toolbar);
        setSupportActionBar(webViewToolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getIntent().getStringExtra("go_title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ArrayList<String>url;
        url = getIntent().getStringArrayListExtra("go_url");
        Log.d("rabanadaaaaaaa",url.get(0));

        mWebView.loadUrl(url.get(0));
        WebSettings webSettings = mWebView.getSettings ();
        webSettings.setJavaScriptEnabled (true);
        webSettings.setBuiltInZoomControls (true);


        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.getSettings().setBuiltInZoomControls(true);







//        String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
//        webView.loadData(customHtml, "text/html", "UTF-8");
        //  mWebView.goBack();
        mWebView.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                load.setVisibility(View.VISIBLE);
                mWebView.loadUrl(url);
                return true;

            }


            public void onPageFinished(WebView view, String url) {
                load.setVisibility(View.GONE);
                super.onPageFinished(view, url);

            }
        });


    }


}
