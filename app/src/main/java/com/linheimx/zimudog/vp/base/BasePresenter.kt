package com.linheimx.zimudog.vp.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables

/**
 * Created by x1c on 2017/5/1.
 */

abstract class BasePresenter {

    var _Disposables: CompositeDisposable

    init {
        this._Disposables = CompositeDisposable()
    }


    fun onDestroy() {
        _Disposables.dispose()
    }
}
