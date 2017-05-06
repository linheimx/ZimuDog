package com.linheimx.zimudog.vp.about;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linheimx.zimudog.R;
import com.linheimx.zimudog.vp.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment {


    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public int _ProvideLayout() {
        return R.layout.fragment_about;
    }

    @Override
    public void _InitPresenter() {

    }

    @Override
    public boolean _OnActivityBackPress() {
        return false;
    }
}
