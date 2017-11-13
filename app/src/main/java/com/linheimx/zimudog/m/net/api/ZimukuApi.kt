package com.linheimx.zimudog.m.net.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by x1c on 2017/5/1.
 */

interface ZimukuApi {

    @GET("/search?ad=1")
    fun getMovieList(@Query("q") movie: String, @Query("p") page: Int): Observable<ResponseBody>

    @GET("{subUrl}")
    fun getHtmlByUrl(@Path("subUrl") subUrl: String): Observable<ResponseBody>

    companion object {
        val BASE_URL = "http://www.zimuku.net"
    }
}
