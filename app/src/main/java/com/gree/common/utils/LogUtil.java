package com.gree.common.utils;

import android.os.Environment;
import android.util.Log;


import com.gree.bean.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author brian @date 2015年7月24日
 */
public class LogUtil {
    private static final String TAG = LogUtil.class.getSimpleName();
    public static boolean mIsDebugMode = Config.DEBUG_ENABLE;

    private static final int JSON_INDENT = 2;

    private static boolean LOGW = true;
    private static boolean LOGE = true;

    public static void v(String tag, String mess) {
        if (mIsDebugMode) { Log.v(tag, buildMessageSafe(mess)); }
    }
    public static void d(String tag, String mess) {
        if (mIsDebugMode) { Log.d(tag, buildMessageSafe(mess)); }
    }
    public static void i(String tag, String mess) {
        if (mIsDebugMode) { Log.i(tag, buildMessageSafe(mess)); }
    }
    public static void w(String tag, String mess) {
        if (LOGW) { Log.w(tag, buildMessageSafe(mess)); }
    }
    public static void e(String tag, String mess) {
        if (LOGE) { Log.e(tag, buildMessageSafe(mess)); }
    }
    public static void log(String mess) {
        if (mIsDebugMode) { Log.i(getTag(), buildMessageSafe(mess)); }
    }
    public static void logError(String mess) {
        if (LOGE) { Log.e(getTag(), buildMessageSafe(mess)); }
    }

    /**
     * write log to file /sdcard/GameCenter.log
     */
    public static void f(String tag, String mess) {
        if (mIsDebugMode) {
            writeFile(Environment.getExternalStorageDirectory().getPath() + System.currentTimeMillis() + ".log", tag, mess);
        }
    }

    /**
     * write log to file /sdcard/GameCenter.log
     */
    public static void f(String mess) {
        f(getTag(), mess);
    }

    /**
     * use the classname as tag
     */
    public static void v(String mess) {
        if (mIsDebugMode) { Log.v(getTag(), buildMessageSafe(mess)); }
    }
    public static void d(String mess) {
        if (mIsDebugMode) { Log.d(getTag(), buildMessageSafe(mess)); }
    }
    public static void i(String mess) {
        if (mIsDebugMode) { Log.i(getTag(), buildMessageSafe(mess)); }
    }
    public static void w(String mess) {
        if (LOGW) { Log.w(getTag(), buildMessageSafe(mess)); }
    }
    public static void e(String mess) {
        if (LOGE) { Log.e(getTag(), buildMessageSafe(mess)); }
    }

    private static final String CLASS_METHOD_LINE_FORMAT = "%s.%s() (%s :%d)";

    /**
     * 显示方法的调用轨迹，过滤关键字“showCallStack”
     */
    public static void showCallStack() {
        if (!mIsDebugMode) {
            return;
        }
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        StringBuilder builder = new StringBuilder();
        for (int i = trace.length - 1; i > 0; i--) {
            builder.append("(" + trace[i].getFileName() + ":" + trace[i].getLineNumber() + ") " + trace[i].getMethodName());
            builder.append(" ——> ");
        }
        builder.append(TAG + ".showCallStack()");
        Log.e(TAG, "showCallStack:" + builder.toString());
    }

    public static void json(String json) {
        if (!mIsDebugMode) return;
        d(getTag(), getPrettyJson(json));
    }

    private static String getPrettyJson(String jsonStr) {
        try {
            jsonStr = jsonStr.trim();
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                return jsonObject.toString(JSON_INDENT);
            }
            if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                return jsonArray.toString(JSON_INDENT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Invalid Json, Please Check: " + jsonStr;
    }

    public static void printError(Throwable throwable) {
        if (mIsDebugMode) {
            throwable.printStackTrace();
        }
    }

    private static String buildMessageSafe(String msg) {
        try {
            return buildMessage(msg);
        } catch (Exception e) {
            printError(e);
        }
        return msg;
    }
    private static String buildMessage(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String caller = "";
        for (int i = 3; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                caller = "(" + trace[i].getFileName() + ":" + trace[i].getLineNumber() + ") "
                        + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread()
                .getId(), caller, msg==null?"":msg);
    }

    private static String getTag() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                break;
            }
        }
        return callingClass;
    }

    private static boolean writeFile(String filePath, String tag, String content) {
        FileWriter fileWriter = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd__HH.mm.ss", Locale.CHINA);
            String time = formatter.format(new Date());
            fileWriter = new FileWriter(filePath, true);
            fileWriter.write(String.format("\n<<<<<<<<<<<<<< %s <<<<<<<<<<<\n", time));
            fileWriter.write(tag + "\n");
            fileWriter.write(content + "\n");
            fileWriter.write(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    printError(e);
                }
            }
        }
    }
}
