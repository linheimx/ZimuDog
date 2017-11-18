package com.linheimx.zimudog.vp.search

import android.util.Log
import com.google.gson.Gson
import com.linheimx.zimudog.m.bean.Resp_Movies
import com.linheimx.zimudog.m.net.ApiManger
import com.linheimx.zimudog.vp.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by x1c on 2017/5/1.
 */

class P_Zimuku(var v: IContract.V) : BasePresenter(), IContract.P {

    var _movie: String = ""
    var _page = 1

    override fun cancelSearch() {
        _Disposables.dispose()
        _Disposables = CompositeDisposable()
    }

    override fun release() {
        _Disposables.dispose()
    }

    override fun searchByKW(keyWord: String) {
        this._movie = keyWord
        this._page = 1
        loadMovies(_movie, _page)
    }

    override fun loadMore() {
        loadMovies(_movie, ++_page)
    }

    private fun loadMovies(movie: String, page: Int) {

        ApiManger.api.getHtml_MovieList(movie, page)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    ApiManger.apiParse.parse_MovieList(it.string())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Resp_Movies> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        _Disposables.add(d)
                    }

                    override fun onNext(it: Resp_Movies) {
                        Log.e("--->", "ret:" + Gson().toJson(it))
                        var page = it.obj
                        if (page == null) {
                            v.showNoDataView()
                        } else {
                            v.showItems(page.movies, page.haveNext)
                        }
                    }

                    override fun onError(it: Throwable) {
                        Log.e("--->", "load error:" + it)
                        v.showLoadingError(it)
                    }
                })
    }
}
