package com.linheimx.zimudog.m.net.downloadZimu;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by x1c on 2017/5/4.
 */

public class ZimuDownloader implements Callable<Boolean> {

    @Override
    public Boolean call() throws Exception {
        Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                System.out.println(bytesRead);
                System.out.println(contentLength);
                System.out.println(done);
                System.out.format("%d%% done\n", (100 * bytesRead) / contentLength);
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
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
