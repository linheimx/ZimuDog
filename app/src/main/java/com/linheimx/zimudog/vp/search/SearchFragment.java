package com.linheimx.zimudog.vp.search;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linheimx.zimudog.R;
import com.linheimx.zimudog.vp.base.BaseFragment;
import com.linheimx.zimudog.vp.base.Provider;
import com.linheimx.zimudog.vp.custom.dialog.LoadingDialog;
import com.linheimx.zimudog.vp.custom.view.SearchBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SearchFragment extends BaseFragment {


    Unbinder _Unbinder;
    Provider _Provider;

    @BindView(R.id.search_bar)
    SearchBar searchBar;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Provider) {
            _Provider = (Provider) context;
        } else {
            throw new RuntimeException("must implement Provider");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        _Provider = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        _Unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchBar.setDrawerLayout(_Provider.provideDrawLayout(), getActivity());
        searchBar.setSearchClickListener(new SearchBar.onSearchClickListener() {
            @Override
            public void onSearchClick(String searchContent) {
                LoadingDialog dialog = new LoadingDialog();
                dialog.show(getChildFragmentManager(), null);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _Unbinder.unbind();
    }


    @Override
    public boolean onActivityBackPress() {
        return false;
    }


}
