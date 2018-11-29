/*
 * Tencent is pleased to support the open source community by making VasSonic available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 *
 */

package com.gree.icleaner.activity.main.sonic;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

import com.gree.common.engine.ZXingHelper;
import com.gree.common.interfaces.OnRequestListener;
import com.gree.common.utils.LocationUtils;
import com.gree.common.utils.LogUtil;
//import com.gree.common.utils.WXPayUtils;
import com.gree.icleaner.activity.main.BrowserActivity;
import com.gree.icleaner.activity.qrcode.zbar.ZBarScanActivity;
import com.tencent.sonic.sdk.SonicDiffDataCallback;

import org.json.JSONObject;

/**
 * Sonic javaScript Interface (Android API Level >= 17)
 */

public class SonicJavaScriptInterface {

    private final SonicSessionClientImpl sessionClient;

    private final Intent intent;

    public static final String PARAM_CLICK_TIME = "clickTime";

    public static final String PARAM_LOAD_URL_TIME = "loadUrlTime";

    public Context mContext;

    public SonicJavaScriptInterface(SonicSessionClientImpl sessionClient, Context mContext, Intent intent) {
        this.sessionClient = sessionClient;
        this.intent = intent;
        this.mContext = mContext;
    }

    @JavascriptInterface
    public void getDiffData() {
        // the callback function of demo page is hardcode as 'getDiffDataCallback'
        getDiffData2("getDiffDataCallback");
    }

    @JavascriptInterface
    public void getDiffData2(final String jsCallbackFunc) {
        if (null != sessionClient) {
            sessionClient.getDiffData(new SonicDiffDataCallback() {
                @Override
                public void callback(final String resultData) {
                    Runnable callbackRunnable = new Runnable() {
                        @Override
                        public void run() {
                            String jsCode = "javascript:" + jsCallbackFunc + "('" + toJsString(resultData) + "')";
                            sessionClient.getWebView().loadUrl(jsCode);
                        }
                    };
                    if (Looper.getMainLooper() == Looper.myLooper()) {
                        callbackRunnable.run();
                    } else {
                        new Handler(Looper.getMainLooper()).post(callbackRunnable);
                    }
                }
            });
        }
    }

    @JavascriptInterface
    public void scanQRCode(final String jsCallbackFunc) {
        LogUtil.e("xxx",jsCallbackFunc);
        if (null != sessionClient) {
            Runnable callbackRunnable = new Runnable() {
                @Override
                public void run() {
                    Activity activity = (Activity) mContext;
                    ZXingHelper.getInstance().toZXingActivity(activity);
                    ZXingHelper.getInstance().setCallback(new OnRequestListener() {
                        @Override
                        public void onOk(String result) {
                            LogUtil.e("code",result);
                            String jsCode = "javascript:" + jsCallbackFunc + "('" + toJsString(result) + "')";
                            sessionClient.getWebView().loadUrl(jsCode);
                        }

                        @Override
                        public void onFail() {
                        }
                    });


                }
            };
            if (Looper.getMainLooper() == Looper.myLooper()) {
                callbackRunnable.run();
            } else {
                new Handler(Looper.getMainLooper()).post(callbackRunnable);
            }
        }
    }

    /**
     * 微信支付
     * @param verifyMsg 获取到的微信验证信息
     * @param jsCallbackFunc 支付回调
     */
//    @JavascriptInterface
//    public void weChatPay(final String verifyMsg,final String jsCallbackFunc) {
//        if (null != sessionClient) {
//            Runnable callbackRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    Activity activity = (Activity) mContext;
//                    WXPayUtils.getInstance().setCallback(new OnRequestListener() {
//                        @Override
//                        public void onOk(String result) {
//                            LogUtil.e("weChatPay",result);
//                            String jsCode = "javascript:" + jsCallbackFunc + "('" + toJsString(result) + "')";
//                            try {
//                                sessionClient.getWebView().loadUrl(jsCode);
//                            }catch (Exception e){
//                                LogUtil.e("XXX",e.toString());
//                            }
//
//                        }
//
//                        @Override
//                        public void onFail() {
//                        }
//                    });
//                    WXPayUtils.getInstance().goToPay(verifyMsg,activity);
//                }
//
//            };
//            if (Looper.getMainLooper() == Looper.myLooper()) {
//                callbackRunnable.run();
//            } else {
//                new Handler(Looper.getMainLooper()).post(callbackRunnable);
//            }
//        }
//    }

    @JavascriptInterface
    public void pubGetLocation(final String jsCallbackFunc) {
        if (null != sessionClient) {
            Runnable callbackRunnable = new Runnable() {
                @Override
                public void run() {
                    Activity activity = (Activity) mContext;
                    LocationUtils.getInstance().setCallback(new OnRequestListener() {
                        @Override
                        public void onOk(String result) {
                            LogUtil.e("location",result);
                            String jsCode = "javascript:" + jsCallbackFunc + "('" + toJsString(result) + "')";
                            sessionClient.getWebView().loadUrl(jsCode);
                        }

                        @Override
                        public void onFail() {
                        }
                    });
                    LocationUtils.getInstance().forLocation(activity);
                }
            };
            if (Looper.getMainLooper() == Looper.myLooper()) {
                callbackRunnable.run();
            } else {
                new Handler(Looper.getMainLooper()).post(callbackRunnable);
            }
        }
    }


    @JavascriptInterface
    public String getPerformance() {
        long clickTime = intent.getLongExtra(PARAM_CLICK_TIME, -1);
        long loadUrlTime = intent.getLongExtra(PARAM_LOAD_URL_TIME, -1);
        try {
            JSONObject result = new JSONObject();
            result.put(PARAM_CLICK_TIME, clickTime);
            result.put(PARAM_LOAD_URL_TIME, loadUrlTime);
            return result.toString();
        } catch (Exception e) {

        }

        return "";
    }

    /*
    * * From RFC 4627, "All Unicode characters may be placed within the quotation marks except
    * for the characters that must be escaped: quotation mark,
    * reverse solidus, and the control characters (U+0000 through U+001F)."
    */
    private static String toJsString(String value) {
        if (value == null) {
            return "null";
        }
        StringBuilder out = new StringBuilder(1024);
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);


            switch (c) {
                case '"':
                case '\\':
                case '/':
                    out.append('\\').append(c);
                    break;

                case '\t':
                    out.append("\\t");
                    break;

                case '\b':
                    out.append("\\b");
                    break;

                case '\n':
                    out.append("\\n");
                    break;

                case '\r':
                    out.append("\\r");
                    break;

                case '\f':
                    out.append("\\f");
                    break;

                default:
                    if (c <= 0x1F) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                    break;
            }

        }
        return out.toString();
    }



}
