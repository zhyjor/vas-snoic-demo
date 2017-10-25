package com.gree.icleaner.activity.loading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.gree.common.engine.CrashHandleHelper;
import com.gree.common.utils.PermissionsUtil;
import com.gree.icleaner.R;
import com.gree.icleaner.activity.loading.presenter.LoadingInitPresenter;
import com.gree.icleaner.activity.loading.view.ILoadingInitView;
import com.gree.icleaner.activity.main.BrowserActivity;
import com.gree.icleaner.activity.qrcode.ForQRcodeActivity;

public class LoadingInitActivity extends Activity implements ILoadingInitView{

    private LoadingInitPresenter initPresenter;
    private Handler mainHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            toActivity();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_loading_init);
        initView();
    }

    protected void initView(){
        initPresenter = new LoadingInitPresenter(LoadingInitActivity.this, LoadingInitActivity.this);
        // 请求权限
        if (PermissionsUtil.checkInitPermission(LoadingInitActivity.this)){
            CrashHandleHelper.getInstance().init(getApplicationContext());
            initPresenter.toHome();
        }

    }
    private void toActivity() {

//        if (!mSettingHelper.getM1LoginSecond()) {
//            Intent intent = new Intent();
//            intent.setClass(LoadingInitActivity.this, LoadingActivity.class);
//            initPresenter.initCCHomeActivity();
//            startActivity(intent);
//            finish();
//        } else {
//            startActivity(new Intent(LoadingInitActivity.this, ForQRcodeActivity.class));
        startActivity(new Intent(LoadingInitActivity.this, BrowserActivity.class));
            finish();
//        }
    }

    @Override
    public void toAct() {
        Message msg = Message.obtain();
        msg.obj = 0;
        msg.what = 0;
        mainHanlder.sendMessage(msg);
    }
}
