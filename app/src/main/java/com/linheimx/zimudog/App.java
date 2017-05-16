package com.linheimx.zimudog;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.linheimx.zimudog.utils.Utils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
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

        /***************************  bugly  *****************************/
        try {
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            String bugly_appid = appInfo.metaData.getString("BUGLY_APPID");// appid

            String channel = appInfo.metaData.getString("UMENG_CHANNEL");// channel
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
            strategy.setAppChannel(channel);

            Bugly.init(getApplicationContext(), bugly_appid, false);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        /*************************  友盟  **********************/
        MobclickAgent.setDebugMode(false);
        PushAgent pushAgent = PushAgent.getInstance(this);
        pushAgent.register(new IUmengRegisterCallback() {  //注册推送服务，每次调用register方法都会回调该接口

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
