package com.linheimx.zimudog.vp.search;

import android.support.annotation.NonNull;

import com.linheimx.lspider.god.GodPage;
import com.linheimx.zimudog.m.net.ApiManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by x1c on 2017/5/1.
 */

public class P_Zimuku extends P_Base implements IContract.P {

    public P_Zimuku(IContract.V v) {
        super(v);
    }

    String _movie;
    int _page = 1;

    @Override
    public void searchByKW(@NonNull String keyWord) {
        this._movie = keyWord;
        this._page = 1;
        loadMovies(_movie, _page);
    }

    @Override
    public void loadMore() {
        loadMovies(_movie, ++_page);
    }

    private void loadMovies(String movie, int page) {

        ApiManager.getInstence()
                .getPage_Zimuku(movie, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GodPage>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        _Disposables.add(d);
                    }

                    @Override
                    public void onNext(GodPage page) {
                        _V.showItems(page.getItemList(), page.isHasMore());
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
}
