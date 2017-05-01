package com.linheimx.zimudog.vp.search;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linheimx.lspider.zimuku.bean.Movie;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.vp.base.BaseFragment;
import com.linheimx.zimudog.vp.base.Provider;
import com.linheimx.zimudog.vp.custom.dialog.LoadingDialog;
import com.linheimx.zimudog.vp.custom.view.SearchBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SearchFragment extends BaseFragment implements IContract.V {

    Unbinder _Unbinder;
    Provider _Provider;

    LoadingDialog _LoadingDialog;

    QuickAdapter _QuickAdapter;
    View notDataView;
    View errorView;

    IContract.P _P;

    @BindView(R.id.search_bar)
    SearchBar searchBar;
    @BindView(R.id.rv)
    RecyclerView rv;

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

        /***************************** presenter **************************/
        _P = new P(this);

        // loading dialog
        _LoadingDialog = new LoadingDialog();
        _LoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                _P.cancelSearch();
            }
        });

        // search bar
        searchBar.setDrawerLayout(_Provider.provideDrawLayout(), getActivity());
        searchBar.setSearchClickListener(new SearchBar.onSearchClickListener() {
            @Override
            public void onSearchClick(String searchContent) {
                if (!TextUtils.isEmpty(searchContent)) {

                    _LoadingDialog.show(getChildFragmentManager(), "dialog");

                    // go
                    _P.searchMovie(searchContent);
                }
            }
        });

        // rv
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        notDataView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) rv.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        errorView = getActivity().getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) rv.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        _QuickAdapter = new QuickAdapter();
        rv.setAdapter(_QuickAdapter);
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


    private void onRefresh() {

    }

    @Override
    public void showMovies(List<Movie> movies) {
        _LoadingDialog.dismiss();
        _QuickAdapter.setNewData(movies);
        Log.e("--->", "go" + movies.size());
    }

    public class QuickAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {

        public QuickAdapter() {
            super(R.layout.layout_animation);
        }

        @Override
        protected void convert(BaseViewHolder helper, Movie item) {
            helper.setImageResource(R.id.img, R.drawable.animation_img1);
            helper.setText(R.id.tweetName, item.getName());
            helper.setText(R.id.tweetText, item.getName_alias());
        }
    }
}
