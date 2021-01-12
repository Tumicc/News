package com.tumi.ala.news.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import com.tumi.ala.news.R;
import com.tumi.ala.news.Utils.MyDatabaseHelper;

public class ShowNewsActivity extends AppCompatActivity {

    private WebView show_news;
    private ImageView collect_news;

    // 添加用户等待显示控件
    private ProgressDialog mDialog;

    private MyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        initView();
        helper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        mDialog = new ProgressDialog(ShowNewsActivity.this);
        mDialog.setMessage("玩命加载ing");

        show_news.setWebViewClient(new WebViewClient() {
            //网页加载时的回调
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }

            //网页停止加载时的回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 如果没有显示，则显示
                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
        show_news.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        final String news_url = intent.getStringExtra("url");
        show_news.loadUrl(news_url);

    }

    private void initView() {
        show_news = findViewById(R.id.show_news);
    }

}


