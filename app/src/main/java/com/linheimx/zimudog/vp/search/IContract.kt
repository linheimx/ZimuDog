package com.linheimx.zimudog.vp.search

/**
 * Created by x1c on 2017/5/1.
 */

interface IContract {

    interface P {
        fun searchByKW(keyWord: String)

        fun loadMore()

        fun cancelSearch()

        fun release()
    }

    interface V {
        fun showItems(movies: List<com.linheimx.zimudog.m.bean.Movie>, hasMore: Boolean)

        fun showLoadingError(ex: Throwable)

        fun showNoDataView()
    }
}
