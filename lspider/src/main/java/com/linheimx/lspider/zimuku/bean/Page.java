package com.linheimx.lspider.zimuku.bean;

import com.linheimx.lspider.god.GodPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public class Page implements GodPage {

    List<Movie> movieList = new ArrayList<>();
    boolean hasMore;

    public Page(List<Movie> movieList, boolean hasMore) {
        this.movieList = movieList;
        this.hasMore = hasMore;
    }

    @Override
    public List<Movie> getItemList() {
        return movieList;
    }

    @Override
    public boolean isHasMore() {
        return hasMore;
    }
}
