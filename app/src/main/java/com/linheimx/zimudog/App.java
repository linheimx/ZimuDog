package com.linheimx.zimudog;

import android.app.Application;
import com.linheimx.zimudog.utils.Utils;

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
    }

    public static App get() {
        return mApp;
    }
}
