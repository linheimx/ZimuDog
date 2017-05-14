package com.linheimx.zimudog.vp.search;

import android.support.annotation.NonNull;

import com.linheimx.lspider.zimuku.bean.Page;
import com.linheimx.zimudog.m.net.ApiManager;
import com.linheimx.zimudog.vp.base.BasePresenter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by x1c on 2017/5/1.
 */

public class P extends BasePresenter implements IContract.P {

    Disposable _Disposable_search;

    IContract.V _V;

    public P(IContract.V v) {
        this._V = v;
    }

    String _movie;
    int _page = 1;

    @Override
    public void searchMovies(@NonNull String movie) {
        this._movie = movie;
        this._page = 1;
        loadMovies(_movie, _page);
    }

    @Override
    public void loadMoreMovies() {
        loadMovies(_movie, ++_page);
    }

    private void loadMovies(String movie, int page) {

        ApiManager.getInstence()
                .getMoviesByKW(movie, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Page>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        _Disposable_search = d;
                        _Disposables.add(d);
                    }

                    @Override
                    public void onNext(Page page) {
                        _V.showMovies(page.getMovieList(), page.isHasMore());
                    }

                    @Override
                    public void onError(Throwable e) {
                        _V.showLoadingError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void cancelSearch() {
        if (_Disposable_search != null) {
            _Disposable_search.dispose();
            _Disposables.delete(_Disposable_search);
        }
    }
}
