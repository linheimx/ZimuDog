package com.linheimx.zimudog.m.net.download.event;

import com.linheimx.lspider.zimuku.bean.Zimu;

import java.util.List;

/**
 * Created by LJIAN on 2017/5/9.
 */

public class EventZimuChanged {
    List<Zimu> zimuList;

    public EventZimuChanged(List<Zimu> zimuList) {
        this.zimuList = zimuList;
    }

    public List<Zimu> getZimuList() {
        return zimuList;
    }

    public void setZimuList(List<Zimu> zimuList) {
        this.zimuList = zimuList;
    }
}
