package com.linheimx.zimudog.vp.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by ari on 8/16/16.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    Unbinder _Unbinder;
    P _p;
    protected Activity _Ac;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(_ProvideLayout(), container, false);
        _Unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _Ac = getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _Unbinder.unbind();
        if (_p != null) {
            _p.onDestroy();
        }
        _Ac = null;
    }

    /**
     * 提供根布局
     *
     * @return
     */
    public abstract int _ProvideLayout();
}
