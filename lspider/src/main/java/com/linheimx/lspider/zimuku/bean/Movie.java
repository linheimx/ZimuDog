package com.linheimx.lspider.zimuku.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public class Movie implements Serializable{
    private String pic_url;// 电影的头像
    private String name;// 电影的名称
    private String name_alias;// 电影的别名
    private List<Zimu> list_zimu = new ArrayList<>();// 电影下的一堆字幕信息
    private String urlMore;//是否有更多字幕

    public void addZimu(Zimu zimu) {
        list_zimu.add(zimu);
    }

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

    public String getName_alias() {
        return name_alias;
    }

    public void setName_alias(String name_alias) {
        this.name_alias = name_alias;
    }

    public List<Zimu> getList_zimu() {
        return list_zimu;
    }

    public void setList_zimu(List<Zimu> list_zimu) {
        this.list_zimu = list_zimu;
    }

    public String getUrlMore() {
        return urlMore;
    }

    public void setUrlMore(String urlMore) {
        this.urlMore = urlMore;
    }
}