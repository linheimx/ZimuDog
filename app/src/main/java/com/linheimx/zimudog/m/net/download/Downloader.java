package com.linheimx.zimudog.m.net.download;

import android.support.annotation.NonNull;
import android.util.Log;

import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior;

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

    State _state;

    public Downloader(@NonNull String key,
                      @NonNull String downloadUrl,
                      @NonNull File outFile) {

        this._key = key;
        this._downloadUrl = downloadUrl;
        this._outFile = outFile;

        _state = new State(_key);
    }

    @Override
    public void run() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(_downloadUrl)
                .get()
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {

                _state.set_nowRead(bytesRead);
                _state.set_nowAll(contentLength);
                _state.set_nowDone(done);

                Log.e("--->", "bytesRead:" + bytesRead);
                Log.e("--->", "contentLength:" + contentLength);
                Log.e("--->", "done:" + done);
                Log.e("--->", "%:" + (100 * bytesRead) / contentLength);

                notifyStateChanged();
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
                // 错误
                _state.setError(true);
                _state.setErrorMsg("不成功，服务器的问题？");
                notifyStateChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 错误
            _state.setError(true);
            _state.setErrorMsg("网络问题？");
            notifyStateChanged();
        }
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String get_downloadUrl() {
        return _downloadUrl;
    }

    public void set_downloadUrl(String _downloadUrl) {
        this._downloadUrl = _downloadUrl;
    }

    public File get_outFile() {
        return _outFile;
    }

    public void set_outFile(File _outFile) {
        this._outFile = _outFile;
    }

    private void notifyStateChanged() {
        RxBus_Behavior.getInstance()
                .post(_state);
    }

    public static class State {

        private String key;
        private long _nowRead;// 已经下载的字节数
        private long _nowAll;// 总的字节数
        private boolean _nowDone;// 是否完成下载啦

        private boolean isError;
        private String errorMsg;

        public State(String key) {
            this.key = key;
        }

        public State(long _nowRead, long _nowAll, boolean _nowDone) {
            this._nowRead = _nowRead;
            this._nowAll = _nowAll;
            this._nowDone = _nowDone;
        }


        public long get_nowRead() {
            return _nowRead;
        }

        public void set_nowRead(long _nowRead) {
            this._nowRead = _nowRead;
        }

        public long get_nowAll() {
            return _nowAll;
        }

        public void set_nowAll(long _nowAll) {
            this._nowAll = _nowAll;
        }

        public boolean is_nowDone() {
            return _nowDone;
        }

        public void set_nowDone(boolean _nowDone) {
            this._nowDone = _nowDone;
        }

        public boolean isError() {
            return isError;
        }

        public void setError(boolean error) {
            isError = error;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
