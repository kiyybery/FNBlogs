package com.xyn.fnblogs.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xyn.fnblogs.base.BaseApplication;
import com.xyn.fnblogs.util.Utils;


/**
 * 监听网络改变
 */
public class NetWorkChangeReceiver extends BroadcastReceiver {
    private BaseApplication mBaseApplication;

    public NetWorkChangeReceiver(BaseApplication baseApplication) {
        mBaseApplication = baseApplication;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mBaseApplication.setNetworkStatus(Utils.GetConnectType(context));
    }
}
