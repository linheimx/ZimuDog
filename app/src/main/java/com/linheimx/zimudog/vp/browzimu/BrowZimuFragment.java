package com.linheimx.zimudog.vp.browzimu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.View;
import android.widget.ImageView;

import com.linheimx.zimudog.R;
import com.linheimx.zimudog.vp.base.BaseFragment;
import com.linheimx.zimudog.vp.base.Provider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BrowZimuFragment extends BaseFragment {


    @BindView(R.id.iv_nav)
    ImageView ivNav;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager viewPager;

    DrawerArrowDrawable _arrowDrawable;

    @Override
    public int _ProvideLayout() {
        return R.layout.fragment_brow;
    }

    @Override
    public void _InitPresenter() {

    }

    @Override
    public boolean _OnActivityBackPress() {
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // iv nav
        _arrowDrawable = new DrawerArrowDrawable(_Ac);
        ivNav.setImageDrawable(_arrowDrawable);
        ivNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Provider) _Ac).provideDrawLayout().openDrawer(GravityCompat.START);

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("file/*");
//                startActivityForResult(intent, 1);
            }
        });

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
