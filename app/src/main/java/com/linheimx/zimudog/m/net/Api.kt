package com.linheimx.zimudog.m.net

import com.linheimx.zimudog.m.bean.Resp_Movies
import com.linheimx.zimudog.m.bean.Resp_Zimus
import com.linheimx.zimudog.m.bean.Resp_Zimus_DURL
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by x1c on 2017/5/1.
 */

interface ZimukuApi {

    @GET("/search?ad=1")
    fun getHtml_MovieList(@Query("q") movie: String, @Query("p") page: Int): Observable<ResponseBody>

    @GET("{subUrl}")
    fun getHtml(@Path("subUrl") subUrl: String): Observable<ResponseBody>

    companion object {
        val BASE_URL = "http://www.zimuku.cn"
    }
}

interface ZimukuParse {

    @FormUrlEncoded
    @POST("api/movielist")
    fun parse_MovieList(@Field("html") html: String): Observable<Resp_Movies>

    @FormUrlEncoded
    @POST("api/zimus")
    fun parse_Zimus(@Field("html") html: String): Observable<Resp_Zimus>

    @FormUrlEncoded
    @POST("api/zimu_download_url")
    fun parse_Zimus_DURL(@Field("html") html: String): Observable<Resp_Zimus_DURL>


    companion object {
        val BASE_URL = "http://111.230.140.111:80/"
    }
}