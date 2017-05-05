package com.linheimx.zimudog.m.net.download;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by x1c on 2017/5/4.
 */

public class Downloader implements Callable<Boolean> {

    private String downloadUrl;
    private File outFile;

    public Downloader(String downloadUrl, File outFile) {
        this.downloadUrl = downloadUrl;
        this.outFile = outFile;
    }

    @Override
    public Boolean call() throws Exception {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .get()
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
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
                Log.e("--->", "下载成功：" + outFile.getAbsolutePath());
                FileOutputStream fileOutputStream = new FileOutputStream(outFile);
                fileOutputStream.write(response.body().bytes());
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
