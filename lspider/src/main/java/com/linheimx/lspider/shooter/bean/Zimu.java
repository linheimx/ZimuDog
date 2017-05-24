package com.linheimx.lspider.shooter.bean;

import com.linheimx.lspider.god.IZimu;

/**
 * Created by x1c on 2017/5/1.
 */

public class Zimu implements IZimu {

    private String name;
    private String alias;
    private String format;
    private String download_page;// 下载链接的界面

    @Override
    public String getPic_url() {
        return "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDownload_page() {
        return download_page;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getFormat() {
        return format;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDownload_page(String download_page) {
        this.download_page = download_page;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}