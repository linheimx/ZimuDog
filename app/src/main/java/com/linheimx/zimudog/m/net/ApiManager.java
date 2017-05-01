package com.linheimx.zimudog.m.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by x1c on 2017/5/1.
 */

public class ApiManager {

    private static OkHttpClient _Client;

    private static ApiManager _ApiManager;

    private ApiManager() {

        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        _Client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new HeaderInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static ApiManager getInstence() {
        if (_ApiManager == null) {
            synchronized (ApiManager.class) {
                if (_ApiManager == null) {
                    _ApiManager = new ApiManager();
                }
            }
        }
        return _ApiManager;
    }


    ZimukuApi _ZimukuApi;

    /**
     * 字幕库相关的api
     *
     * @return
     */
    public ZimukuApi getZimukuApi() {
        if (_ZimukuApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(_Client)
                    .baseUrl(ZimukuApi.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            _ZimukuApi = retrofit.create(ZimukuApi.class);
        }

        return _ZimukuApi;
    }


    public class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .header("User-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .build();
            return chain.proceed(request);
        }
    }
}
