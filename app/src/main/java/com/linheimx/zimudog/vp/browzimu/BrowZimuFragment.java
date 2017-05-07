package com.linheimx.zimudog.vp.browzimu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linheimx.zimudog.R;
import com.linheimx.zimudog.vp.base.BaseFragment;
import com.linheimx.zimudog.vp.base.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BrowZimuFragment extends BaseFragment {


    @BindView(R.id.iv_nav)
    ImageView ivNav;

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

        _arrowDrawable = new DrawerArrowDrawable(_Ac);
        ivNav.setImageDrawable(_arrowDrawable);
        ivNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Provider) _Ac).provideDrawLayout().openDrawer(GravityCompat.START);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, 1);
            }
        });

    }
}
