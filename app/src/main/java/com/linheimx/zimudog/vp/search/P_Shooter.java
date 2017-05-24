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

public class P_Shooter extends P_Base implements IContract.P {


    public P_Shooter(IContract.V v) {
        super(v);
    }

    String _zimu;
    int _page = 1;

    @Override
    public void searchByKW(@NonNull String keyWord) {
        this._zimu = keyWord;
        this._page = 1;
        loadZimus(_zimu, _page);
    }

    @Override
    public void loadMore() {
        loadZimus(_zimu, ++_page);
    }

    private void loadZimus(String zimu, int page) {

        ApiManager.getInstence()
                .getPage_Shooter(zimu, page)
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
