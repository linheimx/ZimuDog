package com.linheimx.zimudog.m.bean

/**
 * Created by x1c on 2017/11/12.
 */

data class Movie(val name: String, val avatar_url: String, val detail_url: String)

data class PageMovie(val movies: List<Movie>, val currentIndex: Int, val haveNext: Boolean)

data class Resp_Movies(val success: Boolean, val errorMsg: String?, val obj: PageMovie?)
