package com.linheimx.lspider.god;

import java.io.Serializable;

/**
 * Created by x1c on 2017/5/22.
 */

public interface IZimu extends GodItem {

    /**
     * 字幕的头像
     *
     * @return
     */
    String getPic_url();

    /**
     * 字幕的名称
     *
     * @return
     */
    String getName();

    /**
     * 字幕的别名
     *
     * @return
     */
    String getAlias();

    /**
     * 字幕的格式
     *
     * @return
     */
    String getFormat();

    /**
     * 这个字幕的下载页面
     *
     * @return
     */
    String getDownload_page();
}
