package com.linheimx.zimudog.vp.search;

import android.support.annotation.NonNull;

import com.linheimx.lspider.god.IMovie;
import com.linheimx.lspider.zimuku.bean.Movie;
import com.linheimx.zimudog.vp.base.IPresenter;

import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public interface IContract {

    interface P extends IPresenter {
        void searchMovies(@NonNull String movie);

        void loadMoreMovies();

        void cancelSearch();
    }

    interface V {
        void showMovies(List<IMovie> movies, boolean hasMore);

        void showLoadingError();
    }
}
