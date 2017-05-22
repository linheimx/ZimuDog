package com.linheimx.lspider.zimuku.bean;

import com.linheimx.lspider.god.IMovie;
import com.linheimx.lspider.god.IZimu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public class Movie implements IMovie {
    private String pic_url;// 电影的头像
    private String name;// 电影的名称
    private String name_alias;// 电影的别名
    private List<Zimu> top5Zimus = new ArrayList<>();// 电影下的一堆字幕 top5
    private List<Zimu> allZimus = new ArrayList<>();// 电影下的所有字幕
    private String allZimuLink;// 更多字幕的链接

    @Override
    public String getPic_url() {
        return pic_url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getName_alias() {
        return name_alias;
    }

    @Override
    public List<Zimu> getTop5Zimus() {
        return top5Zimus;
    }

    @Override
    public List<Zimu> getAllZimus() {
        return allZimus;
    }

    @Override
    public String getAllZimuLink() {
        return allZimuLink;
    }


    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setName_alias(String name_alias) {
        this.name_alias = name_alias;
    }


    public void setTop5Zimus(List<Zimu> top5Zimus) {
        this.top5Zimus = top5Zimus;
    }


    public void setAllZimus(List<Zimu> allZimus) {
        this.allZimus = allZimus;
    }

    public void setAllZimuLink(String allZimuLink) {
        this.allZimuLink = allZimuLink;
    }

    public void addZimu(Zimu zimu) {
        top5Zimus.add(zimu);
    }

}