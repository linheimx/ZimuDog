package com.linheimx.zimudog.m.net.download

import android.util.Log

import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Created by x1c on 2017/5/4.
 */

class Downloader(var _key: String?,
                 var _downloadUrl: String?,
                 var _outFile: File?) : Runnable {

    internal var _state: State

    init {

        _state = State(_key)
    }

    override fun run() {

        val request = Request.Builder()
                .url(_downloadUrl)
                .get()
                .build()

        val progressListener = ProgressListener { bytesRead, contentLength, done ->
            _state._nowRead = bytesRead
            _state._nowAll = contentLength
            _state.is_nowDone = done

            Log.e("--->", "bytesRead:" + bytesRead)
            Log.e("--->", "contentLength:" + contentLength)
            Log.e("--->", "done:" + done)
            Log.e("--->", "%:" + 100 * bytesRead / contentLength)

            notifyStateChanged()
        }

        val client = OkHttpClient.Builder()
                .addNetworkInterceptor { chain ->
                    val originalResponse = chain.proceed(chain.request())
                    originalResponse.newBuilder()
                            .body(ProgressResponseBody(originalResponse.body(), progressListener))
                            .build()
                }
                .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Log.e("--->", "下载成功：" + _outFile!!.absolutePath)
                if (!_outFile!!.exists()) {
                    _outFile!!.createNewFile()
                }
                val fileOutputStream = FileOutputStream(_outFile!!)
                fileOutputStream.write(response.body().bytes())
            } else {
                // 错误
                _state.isError = true
                _state.errorMsg = "不成功，服务器的问题？"
                notifyStateChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 错误
            _state.isError = true
            _state.errorMsg = "网络问题？"
            notifyStateChanged()
        }

    }

    private fun notifyStateChanged() {
        RxBus_Behavior.instance!!
                .post(_state)
    }

    class State {

        var key: String? = null
        var _nowRead: Long = 0// 已经下载的字节数
        var _nowAll: Long = 0// 总的字节数
        var is_nowDone: Boolean = false// 是否完成下载啦

        var isError: Boolean = false
        var errorMsg: String? = null

        constructor(key: String) {
            this.key = key
        }

        constructor(_nowRead: Long, _nowAll: Long, _nowDone: Boolean) {
            this._nowRead = _nowRead
            this._nowAll = _nowAll
            this.is_nowDone = _nowDone
        }
    }
}
