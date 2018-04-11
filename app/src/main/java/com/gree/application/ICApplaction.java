package com.gree.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by XJ on 2016/6/27.
 */
public class ICApplaction extends Application {
    private static String TAG = "ICApplaction";

    /**
     * private
     */
    public static Context mContext; //当前对象
    private static boolean isDebug = false;
    private static boolean isInitFinish = true;

    /**
     * public
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this.getApplicationContext();
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
