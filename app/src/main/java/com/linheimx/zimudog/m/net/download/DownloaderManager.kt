package com.linheimx.zimudog.m.net.download

import com.linheimx.lspider.god.IZimu
import com.linheimx.lspider.zimuku.bean.Zimu
import com.linheimx.zimudog.m.net.download.event.EventZimuChanged
import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior

import java.io.File
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer

/**
 * Created by x1c on 2017/5/6.
 */

class DownloaderManager private constructor() {


    private val _map = HashMap<String, Wrapper>()

    /**
     * 获取正在下载的一堆字幕
     *
     * @return
     */
    private val downloadingZimuList: List<IZimu>
        get() {
            val list = ArrayList<IZimu>()
            for (wrapper in _map.values) {
                list.add(wrapper.zimu)
            }
            return list
        }

    /**
     * 去下载吧，骚年
     *
     * @param pageKey     以下载的页面为key（因为下载的url会经常变化）
     * @param zimu
     * @param downalodUrl
     * @param outFile
     */
    fun startDownload(pageKey: String, zimu: IZimu, downalodUrl: String, outFile: File) {

        if (_map.keys.contains(pageKey)) {
            return
        }

        val downloader = Downloader(pageKey, downalodUrl, outFile)

        _map.put(pageKey, Wrapper(zimu, downloader))
        notifyDataChanged()// 通知下数据改变了

        // 观察这个下载状态
        RxBus_Behavior.instance!!
                .toFlowable(Downloader.State::class.java)
                .subscribe(Consumer<Downloader.State> { state ->
                    if (state.is_nowDone) {  // 这个任务完成了,移除！
                        _map.remove(state.key)
                        notifyDataChanged()
                    }
                })


        // 在线程池里面运行
        executorService.submit(downloader)
    }

    /**
     * 通知这一堆字幕的数据都改变了
     */
    private fun notifyDataChanged() {
        RxBus_Behavior.instance!!
                .post(EventZimuChanged(downloadingZimuList))
    }

    internal inner class Wrapper(var zimu: IZimu, var downloader: Downloader)

    companion object {

        private var _Instance: DownloaderManager? = null
        private val executorService = ThreadPoolExecutor(2, 4,
                60L, TimeUnit.MILLISECONDS,
                LinkedBlockingQueue())

        val instance: DownloaderManager
            get() {
                synchronized(DownloaderManager::class.java) {
                    if (_Instance == null) {
                        _Instance = DownloaderManager()
                    }
                    return _Instance
                }
            }
    }
}
