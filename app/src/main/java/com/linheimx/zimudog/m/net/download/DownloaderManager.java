package com.linheimx.zimudog.m.net.download;

import com.linheimx.lspider.zimuku.bean.Zimu;
import com.linheimx.zimudog.m.net.download.event.EventZimuChanged;
import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by x1c on 2017/5/6.
 */

public class DownloaderManager {

    private static DownloaderManager _Instance;
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    private DownloaderManager() {

    }

    public static DownloaderManager getInstance() {
        synchronized (DownloaderManager.class) {
            if (_Instance == null) {
                _Instance = new DownloaderManager();
            }
            return _Instance;
        }
    }


    private Map<String, Wrapper> _map = new HashMap<>();

    /**
     * 去下载吧，骚年
     *
     * @param pageKey     以下载的页面为key（因为下载的url会经常变化）
     * @param zimu
     * @param downalodUrl
     * @param outFile
     */
    public void startDownload(final String pageKey, Zimu zimu, String downalodUrl, File outFile) {

        if (_map.keySet().contains(pageKey)) {
            return;
        }

        final Downloader downloader = new Downloader(pageKey, downalodUrl, outFile);

        _map.put(pageKey, new Wrapper(zimu, downloader));
        notifyDataChanged();// 通知下数据改变了

        // 观察这个下载状态
        RxBus_Behavior.getInstance()
                .toFlowable(Downloader.State.class)
                .subscribe(new Consumer<Downloader.State>() {
                    @Override
                    public void accept(@NonNull Downloader.State state) throws Exception {
                        if (state.is_nowDone()) {  // 这个任务完成了,移除！
                            _map.remove(state.getKey());
                            notifyDataChanged();
                        }
                    }
                });


        // 在线程池里面运行
        executorService.submit(downloader);
    }

    /**
     * 获取正在下载的一堆字幕
     *
     * @return
     */
    private List<Zimu> getDownloadingZimuList() {
        List<Zimu> list = new ArrayList<>();
        for (Wrapper wrapper : _map.values()) {
            list.add(wrapper.zimu);
        }
        return list;
    }

    /**
     * 通知这一堆字幕的数据都改变了
     */
    private void notifyDataChanged() {
        RxBus_Behavior.getInstance()
                .post(new EventZimuChanged(getDownloadingZimuList()));
    }

    class Wrapper {
        Zimu zimu;
        Downloader downloader;

        public Wrapper(Zimu zimu, Downloader downloader) {
            this.zimu = zimu;
            this.downloader = downloader;
        }
    }
}
