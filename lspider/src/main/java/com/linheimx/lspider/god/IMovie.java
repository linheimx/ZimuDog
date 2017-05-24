package com.linheimx.lspider.god;

import java.util.List;

/**
 * Created by x1c on 2017/5/22.
 */

public interface IMovie<Z extends IZimu> extends GodItem {

    /**
     * 电影的名称
     *
     * @return
     */
    String getName();


    /**
     * 电影的别名
     *
     * @return
     */
    String getName_alias();

    /**
     * 电影的头像
     *
     * @return
     */
    String getPic_url();

    /**
     * top5 的字幕
     * -------------------
     * 一个电影会有一堆字幕，这一堆字幕中最热的5个
     *
     * @return
     */
    List<Z> getTop5Zimus();

    /**
     * 该电影的所有字幕
     *
     * @return
     */
    List<Z> getAllZimus();

    /**
     * 展现所有字幕的链接地址
     *
     * @return
     */
    String getAllZimuLink();
}
