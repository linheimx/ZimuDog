package com.linheimx.zimudog.m.net.download.event;

import com.linheimx.lspider.god.IZimu;
import com.linheimx.lspider.zimuku.bean.Zimu;

import java.util.List;

/**
 * Created by LJIAN on 2017/5/9.
 */

public class EventZimuChanged {
    List<IZimu> zimuList;

    public EventZimuChanged(List<IZimu> zimuList) {
        this.zimuList = zimuList;
    }

    public List<IZimu> getZimuList() {
        return zimuList;
    }

    public void setZimuList(List<IZimu> zimuList) {
        this.zimuList = zimuList;
    }
}
