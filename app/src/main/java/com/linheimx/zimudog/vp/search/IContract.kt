package com.linheimx.zimudog.vp.search

import android.graphics.Movie
import com.linheimx.lspider.god.GodItem
import com.linheimx.lspider.god.IMovie
import com.linheimx.zimudog.vp.base.IPresenter

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
        fun showItems(movies: List<Movie>, hasMore: Boolean)

        fun showLoadingError()
    }
}
