package com.linheimx.lspider.zimuku.bean;

import com.linheimx.lspider.god.IZimu;

import java.io.Serializable;

/**
 * Created by x1c on 2017/5/1.
 */

public class Zimu implements IZimu {
    private String pic_url;// 字幕的头像（国家名称）
    private String name;
    private String download_page;// 下载链接的界面

    @Override
    public String getPic_url() {
        return pic_url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDownload_page() {
        return download_page;
    }


    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDownload_page(String download_page) {
        this.download_page = download_page;
    }
}