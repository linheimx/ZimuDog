package com.linheimx.zimudog.m.net.download;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by x1c on 2017/5/6.
 */

public class DownloaderManager {

    private static DownloaderManager _Instance;

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


    private HashMap<String, Downloader> _map = new HashMap<>();

    public void startDownload(String key, String downalodUrl, File outFile) {

        if(_map.keySet().contains(key)){
            return;
        }

        Downloader downloader = new Downloader(key, downalodUrl, outFile);
        _map.put(key, downloader);

        downloader.run();
    }
}
