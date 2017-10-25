package com.gree.icleaner.activity.loading.presenter;


import com.gree.application.ICApplaction;
import com.gree.icleaner.activity.loading.LoadingInitActivity;
import com.gree.icleaner.activity.loading.view.ILoadingInitView;

/**
 * Created by 680575 on 2016/8/30.
 */
public class LoadingInitPresenter {
    private ILoadingInitView mLoadingInitView;
    private LoadingInitActivity context;

    public LoadingInitPresenter(LoadingInitActivity context, ILoadingInitView mLoadingInitView) {
        this.mLoadingInitView = mLoadingInitView;
        this.context = context;

    }

  /*  *//**
     * 后台登陆任务
     *//*
    public void slientLogin(final String username, final String password, final OnSlientLoginResultListener slientListener) {
        LoginBean loginBean = new LoginBean();
        loginBean.setUser(username);
        loginBean.setPsw(password);
        mLoadingInitModel.slientLogin(loginBean, new OnSLoginResultListener() {
            @Override
            public void onSuccess(LoginResultBean resultBean) {
                if (resultBean == null) {
                    slientListener.onSuccess();
                } else if (resultBean != null && resultBean.getR() == ResultCode.SUCCESS) {
                    LogUtil.e("","静默登录"+resultBean.toString());
                    // 写入到文件中
                    ICApplaction.getUserManagerInstance().setUserInfo(username, password, resultBean.getUid(), resultBean.getUname(), resultBean.getToken(), Constants.ACCOUNT_GREE_LOGIN);
                    GreeDeviceSyncUtil.syncDevice(context);
                    slientListener.onSuccess();
                } else {
                    if (resultBean.getR() == ResultCode.NEED_UER_AUTHORIZATION || resultBean.getR() == ResultCode.LOGIN_FAILED) {
                        slientListener.onFail();
                    } else {
                        slientListener.onSuccess();
                    }
                }
            }

            @Override
            public void onFail() {
                slientListener.onSuccess();
            }
        });
    }

    */

    /**
     * 后台三方登录
     *//*
    public void thirdLogin(FbLoginBean fbloginBean, final OnSlientLoginResultListener slientListener) {
        mLoadingInitModel.setFbLogin(fbloginBean, new OnFacebookListener() {
            @Override
            public void fbUserLoginSuccess(FbLoginResultBean result) {
                if (result == null) {
                    slientListener.onSuccess();
                } else if (result != null && (result.getR() == ResultCode.SUCCESS)) {
                    // 保存用户邮箱
                    ICApplaction.getUserManagerInstance().saveUserEmail(result.getUname(), result.getEmail());
                    // 写入到文件中
                    ICApplaction.getUserManagerInstance().setUserInfo(result.getEmail(), "", result.getUid(), result.getNickName(), result.getToken(), Constants.ACCOUNT_THIRD_LOGIN);
                    ICApplaction.getDeviceManageInstance().setUserInfoForRemote(result.getUid(), result.getToken(), ICApplaction.getServerInstance().getCurrentServer());
                    GreeDeviceSyncUtil.syncDevice(context);
                    slientListener.onSuccess();
                } else {
                    slientListener.onSuccess();
                }
            }

            @Override
            public void fbUserLoginFailed(int errorCode) {
                slientListener.onSuccess();
            }
        });
    }*/

    public void toHome() {
        Thread thread=new Thread(new checkInifThread());
        thread.start();
    }

    class checkInifThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    if (ICApplaction.getInifFinishTag()) {
                        Thread.sleep(1500);
                        mLoadingInitView.toAct();
                        break;
                    }
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
