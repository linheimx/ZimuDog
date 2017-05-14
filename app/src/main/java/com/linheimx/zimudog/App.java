package com.linheimx.zimudog;

import android.app.Application;
import android.util.Log;

import com.linheimx.zimudog.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Created by x1c on 2017/5/6.
 */

public class App extends Application {

    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;
        Utils.mkRootDir();

        /*************************  友盟  **********************/
        MobclickAgent.setDebugMode(true);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {  //注册推送服务，每次调用register方法都会回调该接口

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
    }

    public static App get() {
        return mApp;
    }
}
