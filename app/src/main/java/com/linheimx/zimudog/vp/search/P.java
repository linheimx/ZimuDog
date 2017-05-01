package com.linheimx.zimudog.vp.search;

import android.support.annotation.NonNull;

import com.linheimx.lspider.ParserManager;
import com.linheimx.lspider.zimuku.bean.Movie;
import com.linheimx.zimudog.m.net.ApiManager;
import com.linheimx.zimudog.vp.base.BasePresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

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
    int _page = 0;

    @Override
    public void searchMovies(@NonNull String movie) {
        this._movie = movie;
        this._page = 0;
        loadMovies(_movie, _page);
    }

    @Override
    public void loadMoreMovies() {
        loadMovies(_movie, ++_page);
    }

    private void loadMovies(String movie, int page) {

        ApiManager.getInstence()
                .getZimukuApi()
                .searchMovie(movie, page)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<ResponseBody, ObservableSource<List<Movie>>>() {
                    @Override
                    public ObservableSource<List<Movie>> apply(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        List<Movie> movieList =
                                ParserManager.getInstance().get_MoviesParser().parse(responseBody.string());
                        return Observable.just(movieList);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        _Disposable_search = d;
                        _Disposables.add(d);
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        _V.showMovies(movies);
                    }

                    @Override
                    public void onError(Throwable e) {

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
