package com.linheimx.zimudog.m.bean

import java.io.Serializable

/**
 * Created by x1c on 2017/11/12.
 */

data class Zimu(val name: String, val avatar_url: String, val detail_url: String, val download_url: String) : Serializable

data class Movie(val name: String, val avatar_url: String, val detail_url: String) : Serializable

data class PageMovie(val movies: List<Movie>, val currentIndex: Int, val haveNext: Boolean)

data class Resp_Movies(val success: Boolean, val errorMsg: String?, val obj: PageMovie?)

data class Resp_Zimus(val success: Boolean, val errorMsg: String?, val obj: List<Zimu>?)

data class Resp_Zimus_DURL(val success: Boolean, val errorMsg: String?, val obj: String?)

