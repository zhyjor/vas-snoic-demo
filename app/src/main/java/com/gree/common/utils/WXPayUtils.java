//package com.gree.common.utils;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.gree.common.engine.ZXingHelper;
//import com.gree.common.interfaces.OnRequestListener;
//import com.tencent.mm.opensdk.constants.ConstantsAPI;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
//import com.tencent.mm.opensdk.modelpay.PayReq;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//
//import java.security.MessageDigest;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Random;
//
///**
// * Created by chaohx on 2017/7/5.
// */
//
//public class WXPayUtils  implements IWXAPIEventHandler {
//
//    private static WXPayUtils wXPayUtils;
//    private IWXAPI iwxapi; //微信支付api
//    private WXPayBuilder builder;
//    private OnRequestListener listener;
//    private Context context;
//
//    private WXPayUtils(WXPayBuilder builder) {
//        this.builder = builder;
//    }
//    private WXPayUtils(Context context){
//        this.context = context;
//    }
//    private WXPayUtils(){}
//
//    public static WXPayUtils getInstance() {
//        if (wXPayUtils == null) {
//            wXPayUtils = new WXPayUtils();
//        }
//        return wXPayUtils;
//    }
//
//    public void goToPay(String code, Activity activity){
//        if (PermissionsUtil.checkReadPhoneStatePermission(activity)){
//            WXPayBuilder builder1 = new WXPayBuilder();
//            builder1.setAppId("123")
//                    .setPartnerId("56465")
//                    .setPrepayId("41515")
//                    .setPackageValue("5153")
//                    .setNonceStr("5645")
//                    .setTimeStamp("56512")
//                    .setSign("54615")
//                    .build().toWXPayNotSign(activity,"123");
//        }
//
//
//    }
//
//    public void setCallback(OnRequestListener listener) {
//        this.listener = listener;
//    }
//
//
//
//    /**
//     * 调起微信支付的方法,不需要在客户端签名
//     **/
//    public void toWXPayNotSign(Context context, String appid) {
//        iwxapi = WXAPIFactory.createWXAPI(context, null); //初始化微信api
//        iwxapi.registerApp(appid); //注册appid  appid可以在开发平台获取
//
//        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
//            @Override
//            public void run() {
//                PayReq request = new PayReq(); //调起微信APP的对象
//                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
//                request.appId = builder.getAppId();
//                request.partnerId = builder.getPartnerId();
//                request.prepayId = builder.getPrepayId();
//                request.packageValue = "Sign=WXPay";
//                request.nonceStr = builder.getNonceStr();
//                request.timeStamp = builder.getTimeStamp();
//                request.sign = builder.getSign();
//                Log.e("chx", "run: " + request.appId + request.nonceStr + request.sign);
//                boolean xxx = iwxapi.sendReq(request);//发送调起微信的请求
//
//                if (listener != null) {
//                    listener.onOk("run: " + xxx );
//                }
//
//            }
//        };
//        if (Looper.getMainLooper() == Looper.myLooper()) {
//            payRunnable.run();
//        } else {
//            new Handler(Looper.getMainLooper()).post(payRunnable);
//        }
////        Thread payThread = new Thread(payRunnable);
////        payThread.start();
//    }
//
//    /**
//     * 调起微信支付的方法,需要在客户端签名
//     **/
//    public void toWXPayAndSign(Context context, String appid,final String key) {
//        iwxapi = WXAPIFactory.createWXAPI(context, null); //初始化微信api
//        iwxapi.registerApp(appid); //注册appid  appid可以在开发平台获取
//        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
//            @Override
//            public void run() {
//                if (TextUtils.isEmpty(builder.getAppId())
//                        ||TextUtils.isEmpty(builder.getPartnerId())
//                        ||TextUtils.isEmpty(builder.getPrepayId())){
//                    Log.e("chx", "toWXPayAndSign: "+"必须在builder中设置appId、PartnerId、PrepayId");
//                    return;
//                }
//                PayReq request = new PayReq(); //调起微信APP的对象
//                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
//                request.appId = builder.getAppId();
//                request.partnerId = builder.getPartnerId();
//                request.prepayId = builder.getPrepayId();
//                request.packageValue = "Sign=WXPay";
//                request.nonceStr = genNonceStr();
//                request.timeStamp = String.valueOf(genTimeStamp());
//                request.sign = builder.getSign();
//                //签名
//                LinkedHashMap<String, String> signParams = new LinkedHashMap<>();
//                signParams.put("appid", request.appId);
//                signParams.put("noncestr", request.nonceStr);
//                signParams.put("package", request.packageValue);
//                signParams.put("partnerid", request.partnerId);
//                signParams.put("prepayid", request.prepayId);
//                signParams.put("timestamp", request.timeStamp);
//                request.sign = genPackageSign(signParams,key);
//                iwxapi.sendReq(request);//发送调起微信的请求
//            }
//        };
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }
//
//
//
//
//    // 微信发送请求到第三方应用时，会回调到该方法
//    @Override
//    public void onReq(BaseReq req) {
////        switch (req.getType()) {
////            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
////                goToGetMsg();
////                break;
////            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
////                goToShowMsg((ShowMessageFromWX.Req) req);
////                break;
////            default:
////                break;
////        }
//        LogUtil.e("xxx",req.toString());
//    }
//
//    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
//    @Override
//    public void onResp(BaseResp resp) {
//        int result = 0;
//
//        LogUtil.e("xxx",resp.toString());
//    }
//
//
//    /**
//     * 调起微信APP支付，签名
//     * 生成签名
//     */
//    private   String genPackageSign(LinkedHashMap<String, String> params,String key) {
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String,String> entry: params.entrySet()) {
//            sb.append(entry.getKey());
//            sb.append('=');
//            sb.append(entry.getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        sb.append(key);
//
//        String packageSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        return packageSign;
//    }
//
//    /**
//     * md5加密
//     * @param buffer
//     * @return
//     */
//    private String getMessageDigest(byte[] buffer) {
//        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//        try {
//            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
//            mdTemp.update(buffer);
//            byte[] md = mdTemp.digest();
//            int j = md.length;
//            char str[] = new char[j * 2];
//            int k = 0;
//            for (int i = 0; i < j; i++) {
//                byte byte0 = md[i];
//                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//                str[k++] = hexDigits[byte0 & 0xf];
//            }
//            return new String(str);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//    /**
//     * 获取随机数
//     *
//     * @return
//     */
//    private String genNonceStr() {
//        Random random = new Random();
//        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }
//
//    private WXPayUtils setBuilder(WXPayBuilder wxPayBuilder){
//        this.builder = wxPayBuilder;
//        return this;
//    }
//
//
//    /**
//     * 获取时间戳
//     *
//     * @return
//     */
//    private long genTimeStamp() {
//        return System.currentTimeMillis() / 1000;
//    }
//
//    public static class WXPayBuilder {
//        public String appId;
//        public String partnerId;
//        public String prepayId;
//        public String packageValue;
//        public String nonceStr;
//        public String timeStamp;
//        public String sign;
//
//        public WXPayUtils build() {
//            return WXPayUtils.getInstance().setBuilder(this);
//        }
//
//        public String getAppId() {
//            return appId;
//        }
//
//        public WXPayBuilder setAppId(String appId) {
//            this.appId = appId;
//            return this;
//        }
//
//        public String getPartnerId() {
//            return partnerId;
//        }
//
//        public WXPayBuilder setPartnerId(String partnerId) {
//            this.partnerId = partnerId;
//            return this;
//        }
//
//        public String getPrepayId() {
//            return prepayId;
//        }
//
//        public WXPayBuilder setPrepayId(String prepayId) {
//            this.prepayId = prepayId;
//            return this;
//        }
//
//        public String getPackageValue() {
//            return packageValue;
//        }
//
//        public WXPayBuilder setPackageValue(String packageValue) {
//            this.packageValue = packageValue;
//            return this;
//        }
//
//        public String getNonceStr() {
//            return nonceStr;
//        }
//
//        public WXPayBuilder setNonceStr(String nonceStr) {
//            this.nonceStr = nonceStr;
//            return this;
//        }
//
//        public String getTimeStamp() {
//            return timeStamp;
//        }
//
//        public WXPayBuilder setTimeStamp(String timeStamp) {
//            this.timeStamp = timeStamp;
//            return this;
//        }
//
//        public String getSign() {
//            return sign;
//        }
//
//        public WXPayBuilder setSign(String sign) {
//            this.sign = sign;
//            return this;
//        }
//    }
//
//
//}
