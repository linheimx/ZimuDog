package com.linheimx.zimudog.vp.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linheimx.lcustom.custom.utils.Util;
import com.linheimx.lcustom.custom.view.SearchBar;
import com.linheimx.lspider.zimuku.bean.Movie;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.utils.dialog.ZimuDialog;
import com.linheimx.zimudog.vp.base.BaseFragment;
import com.linheimx.zimudog.vp.base.Provider;

import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements IContract.V {

    QuickAdapter _QuickAdapter;
    View _rv_LoadingView;
    View _rv_ErrorView;

    IContract.P _P;

    @BindView(R.id.search_bar)
    SearchBar searchBar;
    @BindView(R.id.rv)
    RecyclerView _rv;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public int _ProvideLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void _InitPresenter() {
        _P = new P(this);
    }

    @Override
    public boolean _OnActivityBackPress() {
        return false;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // search bar
        searchBar.setDrawerLayout(((Provider) _Ac).provideDrawLayout(), _Ac);
        searchBar.setSearchClickListener(new SearchBar.onSearchClickListener() {
            @Override
            public void onSearchClick(String searchContent) {
                if (!TextUtils.isEmpty(searchContent)) {
                    // 隐藏键盘
                    Util.closeSoftKeyboard(_Ac);

                    _P.cancelSearch();

                    // 处理adapter
                    _QuickAdapter.clearData();
                    _QuickAdapter.setEmptyView(_rv_LoadingView);

                    // go
                    _P.searchMovies(searchContent);
                }
            }
        });

        // _rv
        _rv.setHasFixedSize(true);
        _rv.setLayoutManager(new LinearLayoutManager(_Ac));

        _QuickAdapter = new QuickAdapter();
        _QuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                _P.loadMoreMovies();
            }
        }, _rv);
        _QuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<Movie> movieList = _QuickAdapter.getData();
                Movie movie = movieList.get(position);
                ZimuDialog zimuDialog = ZimuDialog.newInstance(movie);
                zimuDialog.show(getChildFragmentManager(), null);
            }
        });

        _rv_LoadingView = _Ac.getLayoutInflater().inflate(R.layout.rv_loding_view, (ViewGroup) _rv.getParent(), false);
        _rv_LoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        _rv_ErrorView = _Ac.getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) _rv.getParent(), false);
        _rv_ErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        _rv.setAdapter(_QuickAdapter);
    }

    private void onRefresh() {

    }

    @Override
    public void showMovies(List<Movie> movies, boolean hasMore) {
        _QuickAdapter.addData(movies);

        if (hasMore) {
            _QuickAdapter.loadMoreComplete();
        } else {
            _QuickAdapter.loadMoreEnd();
        }

        Log.e("--->", "go" + movies.size());
    }

    public class QuickAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {

        public QuickAdapter() {
            super(R.layout.rv_item_movie);
        }

        public void addData(List<Movie> movies) {
            if (mData == null) {
                mData = movies;
            } else {
                mData.addAll(movies);
            }
            notifyDataSetChanged();
        }

        public void clearData() {
            setNewData(null);
            notifyDataSetChanged();
        }

        @Override
        protected void convert(final BaseViewHolder helper, Movie item) {

            Glide.with(SearchFragment.this)
                    .load(item.getPic_url())
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.img));

            helper.setText(R.id.tv_title, item.getName());
            helper.setText(R.id.tweetText, item.getName_alias());
        }
    }
}
