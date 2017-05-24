package com.linheimx.zimudog.vp.browzimu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.linheimx.zimudog.R;
import com.linheimx.zimudog.vp.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BrowZimuFragment extends BaseFragment {

    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager viewPager;

    @Override
    public int _ProvideLayout() {
        return R.layout.fragment_brow;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // viewpager
        DownloadingFragment downloadingFragment = new DownloadingFragment();
        SdCardFragment sdCardFragment = new SdCardFragment();

        List<TitleFragment> list = new ArrayList<>();
        list.add(sdCardFragment);
        list.add(downloadingFragment);
        
        VPAdapter vpAdapter = new VPAdapter(getChildFragmentManager(), list);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOffscreenPageLimit(2);

        // tab
        tabLayout.setupWithViewPager(viewPager);
    }
}
