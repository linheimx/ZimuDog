package com.linheimx.zimudog.vp.search;

import android.support.annotation.NonNull;

import com.linheimx.lspider.zimuku.bean.Movie;

import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public interface IContract {

    interface P {
        void searchMovies(@NonNull String movie);

        void loadMoreMovies();

        void cancelSearch();

        void onDestroy();
    }

    interface V {
        void showMovies(List<Movie> movies, boolean hasMore);
    }
}
