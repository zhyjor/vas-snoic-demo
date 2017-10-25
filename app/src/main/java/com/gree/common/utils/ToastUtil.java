package com.gree.common.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;


/**
 * 常用Toast提醒，避免Toast多次提醒问题
 * Created by Czg on 2016/7/4.
 */
public class ToastUtil {
    private static long time;
    private static String oldMsg;
    private static void showToast(Context context, String msg, int duration) {
        try {
            func(context,msg,duration);
        }catch (Exception e){
            LogUtil.e("ToastUtil",e.toString());
            Looper.prepare();
            func(context,msg,duration);
            Looper.loop();
        }
    }

    private static void func(Context context, String msg, int duration){
        if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
            Toast.makeText(context, msg, duration).show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于2秒时才显示
            if (System.currentTimeMillis() - time > 2000) {
                Toast.makeText(context, msg, duration).show();
                time = System.currentTimeMillis();
            }
        }
        oldMsg = msg;
    }

    /**
     * 显示一段较长时间的提示
     *
     * @param context
     * @param res
     */
    public static void showLong(Context context, int res) {
        showLong(context, context.getString(res));
    }

    public static void showLong(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 显示一段较短的提示
     *
     * @param context
     * @param res
     */
    public static void showShort(Context context, int res) {
        showShort(context, context.getString(res));
    }

    public static void showShort(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }
}
