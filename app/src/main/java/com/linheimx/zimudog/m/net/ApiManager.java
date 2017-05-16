package com.linheimx.zimudog.m.net;

import com.linheimx.lspider.ParserManager;
import com.linheimx.lspider.zimuku.bean.Page;
import com.linheimx.zimudog.m.net.api.ZimukuApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
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

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

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
    private ZimukuApi zimukuApi() {
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


    /**
     * 通过关键词，获得一堆电影
     *
     * @param movie
     * @param page
     * @return
     */
    public Observable<Page> getMoviesByKW(String movie, int page) {
        return zimukuApi()
                .searchMovie(movie, page)
                .flatMap(new Function<ResponseBody, ObservableSource<Page>>() {
                    @Override
                    public ObservableSource<Page> apply(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        Page page =
                                ParserManager.getInstance().get_MoviesParser().parse(responseBody.string());
                        return Observable.just(page);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取字幕的下载链接
     *
     * @param downloadPageUrl
     * @return
     */
    public Observable<String> getDownloadUrlForZimu(final String downloadPageUrl) {
        return zimukuApi()
                .getDownloadPage(downloadPageUrl)
                .flatMap(new Function<ResponseBody, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull ResponseBody responseBody) throws Exception {
                        String downloadUrl =
                                ParserManager.getInstance().get__ZimuDownloadPageParser().parse(responseBody.string());
                        return Observable.just(downloadUrl);
                    }
                })
                .subscribeOn(Schedulers.io());
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
