package com.linheimx.lspider.zimuku.bean;

import com.linheimx.lspider.god.IMovie;
import com.linheimx.lspider.god.IPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public class Page implements IPage {

    List<Movie> movieList = new ArrayList<>();
    boolean hasMore;

    public Page(List<Movie> movieList, boolean hasMore) {
        this.movieList = movieList;
        this.hasMore = hasMore;
    }

    @Override
    public List<Movie> getMovieList() {
        return movieList;
    }

    @Override
    public boolean isHasMore() {
        return hasMore;
    }
}
