package com.gree.application;

import android.app.Application;

/**
 * Created by XJ on 2016/6/27.
 */
public class ICApplaction extends Application {
    private static String TAG = "ICApplaction";

    /**
     * private
     */
    private static ICApplaction context; //当前对象
    private static boolean isDebug = false;
    private static boolean isInitFinish = true;

    /**
     * public
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static boolean getInifFinishTag() {
        return isInitFinish;
    }

    /**
     * 获取当前是否是调试模式
     **/
    public static boolean getIsDebug() {
        return isDebug;
    }
}
