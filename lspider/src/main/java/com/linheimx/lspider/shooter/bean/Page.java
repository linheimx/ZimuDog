package com.linheimx.lspider.shooter.bean;

import com.linheimx.lspider.god.GodPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public class Page implements GodPage {

    List<Zimu> movieList = new ArrayList<>();
    boolean hasMore;

    public Page(List<Zimu> zimuList, boolean hasMore) {
        this.movieList = zimuList;
        this.hasMore = hasMore;
    }

    @Override
    public List<Zimu> getItemList() {
        return movieList;
    }

    @Override
    public boolean isHasMore() {
        return hasMore;
    }
}
