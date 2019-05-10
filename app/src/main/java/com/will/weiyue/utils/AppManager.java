package com.will.weiyue.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.SparseArray;

import java.util.LinkedList;
import java.util.Stack;

/**
 * @author
 * @version 1.0
 * @Date 2014-2-5
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     */
    public void finishAllActivity() {
        for (Activity anActivityStack : activityStack) {
            if (null != anActivityStack) {
                anActivityStack.finish();
            }
        }
        activityStack.clear();
    }


    /**
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    private SparseArray<LinkedList<Activity>> actions = new SparseArray<LinkedList<Activity>>();

    public void addAction(int taskTag, Activity activity) {
        LinkedList<Activity> as = actions.get(taskTag);
        if (as == null) {
            as = new LinkedList<Activity>();
            actions.put(taskTag, as);
        }
        as.add(activity);
    }

    // 一系列的任务标志 1很多的1 activity直接干掉
    public void finishTask(int taskTag) {
        LinkedList<Activity> as = actions.get(taskTag);
        if (as != null) {
            for (Activity a : as) {
                a.finish();
            }
        }
        actions.remove(taskTag);
    }

}
