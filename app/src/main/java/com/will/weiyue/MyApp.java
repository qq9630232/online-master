package com.will.weiyue;


import com.will.weiyue.bean.User;
import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.component.DaggerApplicationComponent;
import com.will.weiyue.module.ApplicationModule;
import com.will.weiyue.module.HttpModule;
import com.will.weiyue.utils.AppManager;
import com.will.weiyue.utils.ContextUtils;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.jpush.android.api.JPushInterface;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
public class MyApp extends LitePalApplication {

    private ApplicationComponent mApplicationComponent;

    private static MyApp sMyApp;

    public static int width = 0;

    public static int height = 0;
    private String appId = "e7c24f9e2ea4b40906480cf6b9e70fbe";

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApp = this;
        BGASwipeBackManager.getInstance().init(this);
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();
        LitePal.initialize(this);
        width = ContextUtils.getSreenWidth(MyApp.getContext());
        height = ContextUtils.getSreenHeight(MyApp.getContext());
        Bmob.initialize(this, appId);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        closeAndroidPDialog();
    }
    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static MyApp getInstance() {
        return sMyApp;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
    /**
     * 退出登录,清空缓存数据
     */
    public void logout() {
        BmobUser.logOut();
        User.logOut();
        AppManager.getAppManager().finishAllActivity();
    }
}
