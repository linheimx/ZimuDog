package com.linheimx.zimudog

import android.app.Application
import com.linheimx.zimudog.utils.Utils

/**
 * Created by x1c on 2017/5/6.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        mApp = this
        Utils.init()
    }

    companion object {

        private var mApp: App? = null

        fun get(): App? {
            return mApp
        }
    }
}
