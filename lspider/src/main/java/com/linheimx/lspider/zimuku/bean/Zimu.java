package com.linheimx.lspider.zimuku.bean;

import java.io.Serializable;

/**
 * Created by x1c on 2017/5/1.
 */

public class Zimu implements Serializable {
    private String pic_url;// 字幕的头像（国家名称）
    private String name;
    private String download_page;// 下载链接的界面
    private String download_url;// 需要去获取一个页面，再解析，考虑延迟去加载吧

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getDownload_page() {
        return download_page;
    }

    public void setDownload_page(String download_page) {
        this.download_page = download_page;
    }
}