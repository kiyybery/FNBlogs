package com.xyn.fnblogs.base;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.xyn.fnblogs.R;
import com.xyn.fnblogs.ui.ScrollWebView;
import com.xyn.fnblogs.util.WebViewJsInterface;

/**
 * Created by Administrator on 2016/3/23 0023.
 */
public abstract class BaseSingleWebView extends BaseActivity implements ScrollWebView.OnScrollListener {

    protected ScrollWebView mWebView;
    private FrameLayout mWebViewContainer;
    private View mProgress;
    private int mPreviousYPos;

    @Override
    protected boolean isSwipeToClose() {
        return true;
    }

    protected abstract int getLayoutId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        mProgress = findViewById(R.id.news_detail_progress);
        mWebViewContainer = (FrameLayout) findViewById(R.id.html_detail_web_view);
        mWebView = new ScrollWebView(this);
        mWebViewContainer.addView(mWebView);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.addJavascriptInterface(new WebViewJsInterface(this), "Android");
        mWebView.setOnScrollListener(this);
    }

    @Override
    protected void onDestroy() {
        mWebView.setOnScrollListener(null);
        mWebView.destroy();
        mWebViewContainer.removeAllViews();
        super.onDestroy();
    }

    protected void renderProgress(boolean isShow) {
        if (isShow)
            mProgress.setVisibility(View.VISIBLE);
        else
            mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onScroll(int x, int y) {
        switchActionBar(y - mPreviousYPos);
        mPreviousYPos = y;
    }
}
