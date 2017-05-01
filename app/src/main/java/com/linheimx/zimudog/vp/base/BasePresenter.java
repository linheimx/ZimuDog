package com.linheimx.zimudog.vp.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposables;

/**
 * Created by x1c on 2017/5/1.
 */

public abstract class BasePresenter {

    public CompositeDisposable _Disposables;

    public BasePresenter() {
        this._Disposables = new CompositeDisposable();
    }


    public void onDestroy() {
        _Disposables.dispose();
    }
}
