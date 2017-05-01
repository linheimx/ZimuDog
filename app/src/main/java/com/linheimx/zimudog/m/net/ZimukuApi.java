package com.linheimx.zimudog.m.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by x1c on 2017/5/1.
 */

public interface ZimukuApi {
    String BASE_URL = "http://www.zimuku.net";

    @GET("/search?ad=1")
    Observable<ResponseBody> searchMovie(@Query("q") String movie, @Query("p") int page);
}
