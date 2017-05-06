package com.linheimx.zimudog.m.net.download;

import android.util.Log;

import com.linheimx.zimudog.utils.rxbus.RxBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by x1c on 2017/5/4.
 */

public class Downloader implements Runnable {
    private String _key;
    private String _downloadUrl;
    private File _outFile;

    public Downloader(String key, String downloadUrl, File outFile) {
        this._key = key;
        this._downloadUrl = downloadUrl;
        this._outFile = outFile;
    }

    @Override
    public void run() {

        Request request = new Request.Builder()
                .url(_downloadUrl)
                .get()
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {

                RxBus.getInstance()
                        .post(new ProgressEvent(_key, bytesRead, contentLength));

                Log.e("--->", "bytesRead:" + bytesRead);
                Log.e("--->", "contentLength:" + contentLength);
                Log.e("--->", "done:" + done);
                Log.e("--->", "%:" + (100 * bytesRead) / contentLength);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("--->", "下载成功：" + _outFile.getAbsolutePath());
                FileOutputStream fileOutputStream = new FileOutputStream(_outFile);
                fileOutputStream.write(response.body().bytes());
            } else {

                RxBus.getInstance()
                        .post(new ProgressEvent(_key, true, response.message()));
            }
        } catch (Exception e) {
            e.printStackTrace();

            RxBus.getInstance()
                    .post(new ProgressEvent(_key, true, e.getMessage()));
        }
    }


}
