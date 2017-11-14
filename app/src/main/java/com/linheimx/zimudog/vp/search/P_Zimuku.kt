package com.linheimx.zimudog.vp.search

import com.linheimx.zimudog.vp.base.BasePresenter
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by x1c on 2017/5/1.
 */

class P_Zimuku(v: IContract.V) : BasePresenter(), IContract.P {

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

//        ApiManager2222.instence
//                .getPage_Zimuku(movie, page)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<GodPage<*>> {
//                    override fun onSubscribe(d: Disposable) {
//                        _Disposables.add(d)
//                    }
//
//                    override fun onNext(page: GodPage<*>) {
//                        _V.showItems(page.itemList, page.isHasMore)
//                    }
//
//                    override fun onError(e: Throwable) {
//                        _V.showLoadingError()
//                    }
//
//                    override fun onComplete() {
//
//                    }
//                })
    }
}
