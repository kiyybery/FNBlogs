package com.xyn.fnblogs.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/3/23 0023.
 */
public class ScrollWebView extends WebView {

    private WeakReference<OnScrollListener> mOnScrollListener;

    public ScrollWebView(final Context context) {
        super(context);
    }

    public ScrollWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        OnScrollListener listener = mOnScrollListener.get();

        if (listener == null) {
            Log.d("被释放", mOnScrollListener.toString());
            return;
        }
        listener.onScroll(l, t);
    }

    public void setOnScrollListener(final OnScrollListener onScrollListener) {
        mOnScrollListener = new WeakReference<>(onScrollListener);
    }

    public interface OnScrollListener {
        void onScroll(int x, int y);
    }
}
