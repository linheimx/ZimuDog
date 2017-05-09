package com.linheimx.zimudog.vp.browzimu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJIAN on 2017/5/9.
 */

public class VPAdapter extends FragmentPagerAdapter {

    List<TitleFragment> _list = new ArrayList<>();

    public VPAdapter(FragmentManager fm, List<TitleFragment> list) {
        super(fm);
        this._list = list;
    }

    @Override
    public int getCount() {
        return _list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return _list.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _list.get(position).getTitle();
    }
}
