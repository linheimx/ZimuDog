package com.linheimx.lspider.god;

import java.io.Serializable;
import java.util.List;

/**
 * Created by x1c on 2017/5/22.
 */

public interface GodPage<M extends GodItem> extends Serializable {


    /**
     * 获得该页的一堆 item
     *
     * @return
     */
    List<M> getItemList();

    /**
     * 是否有更多的数据（更多的页）
     *
     * @return
     */
    boolean isHasMore();
}
