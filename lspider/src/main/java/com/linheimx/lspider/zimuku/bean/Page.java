package com.linheimx.lspider.zimuku.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public class Page {

    List<Movie> movieList = new ArrayList<>();
    boolean hasMore;//有更多的数据


    public Page() {
    }

    public Page(List<Movie> movieList, boolean hasMore) {
        this.movieList = movieList;
        this.hasMore = hasMore;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
