package com.xyn.fnblogs.image;


import com.xyn.fnblogs.base.BaseApplication;
import com.xyn.fnblogs.lib.ZHttp;
import com.xyn.fnblogs.util.DBHelper;

/**
 * 图片下载功能
 */
public class SaveImageTask extends BaseImageAsyncTask {
    private BaseApplication baseApp;
    private String url;
    private int width;
    private int height;

    public SaveImageTask(BaseApplication baseApp, String url, int width, int height) {
        this.baseApp = baseApp;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @Override
    public void run() {
        if (baseApp.isNetworkWifi()) {
            byte[] bytes = ZHttp.getBytes(url);

            if (bytes != null && bytes.length > 0) {
                DBHelper.cache().save(url, bytes);
            }
        }
        ImageTaskManager.getInstance().Done(getTaskId());
    }

    @Override
    public int getTaskId() {
        return SAVE_IMAGE_TASK_ID;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
