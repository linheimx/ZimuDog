package com.linheimx.zimudog.vp.search;

import com.linheimx.zimudog.vp.base.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by x1c on 2017/5/1.
 */

public abstract class P_Base extends BasePresenter implements IContract.P {

    IContract.V _V;

    public P_Base(IContract.V v) {
        this._V = v;
    }


    @Override
    public void cancelSearch() {
        _Disposables.dispose();
        _Disposables = new CompositeDisposable();
    }

    @Override
    public void release() {
        _Disposables.dispose();
        _Disposables = null;
    }

}
