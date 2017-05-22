package com.linheimx.lspider.god;

import com.linheimx.lspider.zimuku.bean.Movie;

import java.io.Serializable;
import java.util.List;

/**
 * Created by x1c on 2017/5/22.
 */

public interface IPage<M extends IMovie> extends Serializable {


    /**
     * 获得该页的一堆电影
     *
     * @return
     */
    List<M> getMovieList();

    /**
     * 是否有更多的数据（更多的页）
     *
     * @return
     */
    boolean isHasMore();
}
