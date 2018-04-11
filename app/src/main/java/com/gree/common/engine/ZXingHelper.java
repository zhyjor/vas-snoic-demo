package com.gree.common.engine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.gree.common.interfaces.OnRequestListener;
import com.gree.icleaner.activity.qrcode.zbar.ZBarScanActivity;

/**
 * 二维码扫描工具类
 *
 * Created by zhyjor on 2017/10/26.
 */

public class ZXingHelper {

    private static ZXingHelper zXingHelper;
    private int requestCode;
    private OnRequestListener listener;

    private ZXingHelper() {
    }

    public static ZXingHelper getInstance() {
        if (zXingHelper == null) {
            zXingHelper = new ZXingHelper();
        }
        return zXingHelper;
    }

    public void toZXingActivity(Activity activity){
        requestCode = 0;
        activity.startActivityForResult(new Intent(activity,ZBarScanActivity.class),requestCode);

    }

    /**
     * 在Activity的OnActivityResult调用
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void addActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {

        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 0:
                String code = intent.getStringExtra("code");
                if (listener != null) {
                    listener.onOk(code);
                }
                break;
            default:
                break;
        }
    }

    public void setCallback(OnRequestListener listener) {
        this.listener = listener;
    }

    /**
     * 销毁
     */
    public void removeCallback() {
        listener = null;
    }
}
