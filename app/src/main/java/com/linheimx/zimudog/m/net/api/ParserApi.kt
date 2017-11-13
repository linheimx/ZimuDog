package com.linheimx.zimudog.m.net.api

import retrofit2.http.POST

/**
 * Created by x1c on 2017/11/12.
 */
interface ParserApi{

    @POST()
    fun MovieList()

    companion object {
        val BASE_URL = "http://www.zimuku.net"
    }
}