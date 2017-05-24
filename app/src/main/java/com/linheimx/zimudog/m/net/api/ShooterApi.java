package com.linheimx.zimudog.m.net.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by x1c on 2017/5/1.
 */

public interface ShooterApi {
    String BASE_URL = "https://secure.assrt.net";

    @GET("/sub/")
    Observable<ResponseBody> searchZimu(@Query("searchword") String movie, @Query("page") int page);

    @GET("{subUrl}")
    Observable<ResponseBody> getHtmlByUrl(@Path("subUrl") String subUrl);
}
