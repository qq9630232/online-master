package com.will.weiyue.utils.download;

import android.content.Context;

import java.io.File;

/**
 * Created by SDC on 2019/5/9.
 */

public class IndexContract {

    public interface View {
        void showUpdate(String version);
        void showProgress(int progress);
        void showFail(String msg);
        void showComplete(File file);
    }

    interface Presenter{
        void checkUpdate(String local);
        void setIgnore(String version);
        void downApk(Context context,String apkUrl);
        void unbind(Context context);
    }
}
