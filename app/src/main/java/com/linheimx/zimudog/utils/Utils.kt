package com.linheimx.zimudog.utils

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View

import com.linheimx.zimudog.App
import zlc.season.rxdownload3.core.DownloadConfig

import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Method

/**
 * Created by x1c on 2017/5/4.
 */

object Utils {

    val rootDirPath: String
        get() = Environment.getExternalStorageDirectory().toString() + "/ZimuDog"

    /**
     * 检测网络是否连接
     *
     * @return
     */
    // 当前网络是连接的
    // 当前所连接的网络可用
    val isNetConnected: Boolean
        get() {

            val connectivity = App.get()!!
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.activeNetworkInfo
                if (info != null && info.isConnected) {
                    if (info.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
            return false
        }


    fun mkRootDir() {
        try {
            val filePath = Environment.getExternalStorageDirectory().toString() + "/ZimuDog"
            val file = File(filePath)
            if (!file.exists()) {
                file.mkdir()
            }
            initRxDownloader(App.get()!!, filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun initRxDownloader(context: Application, basePath: String) {
        val builder = DownloadConfig.Builder.create(context)
                .enableDb(true)
                .enableAutoStart(true)
                .enableNotification(true)
                .enableService(true)
                .setDefaultPath(basePath)
        DownloadConfig.init(builder)
    }


    /**
     * 文件大小---》显示
     *
     * @param size
     * @return
     */
    fun convertFileSize(size: Long): String {
        val kb: Long = 1024
        val mb = kb * 1024
        val gb = mb * 1024

        if (size >= gb) {
            return String.format("%.1f GB", size.toFloat() / gb)
        } else if (size >= mb) {
            val f = size.toFloat() / mb
            return String.format(if (f > 100) "%.0f MB" else "%.1f MB", f)
        } else if (size >= kb) {
            val f = size.toFloat() / kb
            return String.format(if (f > 100) "%.0f KB" else "%.1f KB", f)
        } else
            return String.format("%d B", size)
    }

    fun loadBitmapFromView(v: View): Bitmap {
        val b = Bitmap.createBitmap(v.layoutParams.width, v.layoutParams.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }

    /**
     * Draw the view into a bitmap.
     */
    fun getViewBitmap(v: View): Bitmap? {
        v.clearFocus()
        v.isPressed = false

        val willNotCache = v.willNotCacheDrawing()
        v.setWillNotCacheDrawing(false)

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        val color = v.drawingCacheBackgroundColor
        v.drawingCacheBackgroundColor = 0

        if (color != 0) {
            v.destroyDrawingCache()
        }
        v.buildDrawingCache()
        val cacheBitmap = v.drawingCache
        if (cacheBitmap == null) {
            Log.e("v---> bm", "failed getViewBitmap($v)", RuntimeException())
            return null
        }

        val bitmap = Bitmap.createBitmap(cacheBitmap)

        // Restore the view
        v.destroyDrawingCache()
        v.setWillNotCacheDrawing(willNotCache)
        v.drawingCacheBackgroundColor = color

        return bitmap
    }


    fun checkPermission(context: Context, permission: String): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                val clazz = Class.forName("android.content.Context")
                val method = clazz.getMethod("checkSelfPermission", String::class.java)
                val rest = method.invoke(context, permission) as Int
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true
                } else {
                    result = false
                }
            } catch (e: Exception) {
                result = false
            }

        } else {
            val pm = context.packageManager
            if (pm.checkPermission(permission, context.packageName) == PackageManager.PERMISSION_GRANTED) {
                result = true
            }
        }
        return result
    }

//    @Throws(IOException::class)
//    fun getStringFromInputStream(`is`: InputStream?): String {
//        val baos = ByteArrayOutputStream()
//        val buffer = ByteArray(1024)
//        var length: Int
//        while ((length = `is`!!.read(buffer)) != -1) {
//            baos.write(buffer, 0, length)
//        }
//        return baos.toString("UTF-8")
//    }

//    fun getStringFromAssetFile(asset: AssetManager, filename: String): String {
//        var `is`: InputStream? = null
//
//        try {
//            `is` = asset.open(filename)
//            return getStringFromInputStream(`is`)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return ""
//        } finally {
//            if (`is` != null) {
//                try {
//                    `is`.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//            }
//        }
//    }

//    fun getStringFromFile(file: File): String {
//        var `is`: InputStream? = null
//
//        try {
//            `is` = FileInputStream(file)
//            return getStringFromInputStream(`is`)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return ""
//        } finally {
//            if (`is` != null) {
//                try {
//                    `is`.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//            }
//        }
//    }


}
