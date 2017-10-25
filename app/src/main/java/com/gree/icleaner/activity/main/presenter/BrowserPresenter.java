package com.gree.icleaner.activity.main.presenter;



import android.content.Intent;

import com.gree.icleaner.activity.main.BrowserActivity;
import com.gree.icleaner.activity.qrcode.zbar.ZBarScanActivity;

/**
 * Created by 680575 on 2016/8/30.
 */
public class BrowserPresenter {
    private BrowserActivity context;

    public BrowserPresenter(BrowserActivity context) {
        this.context = context;
    }


    public void toActivity() {
        context.startActivity(new Intent(context, ZBarScanActivity.class));
    }

}
