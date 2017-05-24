package com.linheimx.zimudog.vp.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.linheimx.lcustom.custom.utils.Util;
import com.linheimx.lcustom.custom.view.SearchBar;
import com.linheimx.lspider.god.GodItem;
import com.linheimx.lspider.god.IMovie;
import com.linheimx.lspider.god.IZimu;
import com.linheimx.zimudog.App;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.bean.event.Event_ShowNav;
import com.linheimx.zimudog.utils.ContantUtil;
import com.linheimx.zimudog.utils.rxbus.RxBus;
import com.linheimx.zimudog.vp.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import es.dmoral.prefs.Prefs;

public class SearchFragment extends BaseFragment implements IContract.V {

    BaseQuickAdapter _QuickAdapter;

    IContract.P _P;
    String _selectSource;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init data
        _selectSource = Prefs.with(App.get()).read(ContantUtil.SELECT_SOURCE, ContantUtil.ZIMUKU);

        // search bar
        searchBar.setSearchClickListener(new SearchBar.onSearchBarClickListener() {
            @Override
            public void onSearchClick(String searchContent) {
                searchKeyWord(searchContent);
            }

            @Override
            public void onMenuClick() {
                showMenu();
            }
        });

        // _rv
        _rv.setHasFixedSize(true);
        _rv.setLayoutManager(new LinearLayoutManager(_Ac));
        chooseOne();
        showEmptyView();
    }


    /**
     * 选择一个逻辑
     */
    private void chooseOne() {
        if (_P != null) {
            _P.release();
        }
        if (_selectSource.equals(ContantUtil.SHOOTER)) {
            setAdapter4shooter();
            _P = new P_Shooter(this);
        } else if (_selectSource.equals(ContantUtil.ZIMUKU)) {
            setAdapter4zimuku();
            _P = new P_Zimuku(this);
        }
    }

    private void setAdapter4zimuku() {
        _QuickAdapter = new QuickAdapter_ZimuKu();
        _QuickAdapter.bindToRecyclerView(_rv);
        _QuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                _P.loadMore();
            }
        }, _rv);
        _QuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<IMovie> movieList = _QuickAdapter.getData();
                IMovie movie = movieList.get(position);
                ZimuDialog zimuDialog = ZimuDialog.newInstance(movie);
                zimuDialog.show(getChildFragmentManager(), null);
            }
        });
    }

    private void setAdapter4shooter() {
        _QuickAdapter = new QuickAdapter_Shooter();
        _QuickAdapter.bindToRecyclerView(_rv);
        _QuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                _P.loadMore();
            }
        }, _rv);
        _QuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                List<IMovie> movieList = _QuickAdapter.getData();
//                IMovie movie = movieList.get(position);
//                ZimuDialog zimuDialog = ZimuDialog.newInstance(movie);
//                zimuDialog.show(getChildFragmentManager(), null);
            }
        });
    }

    @Override
    public void showLoadingError() {
        showErrorView();
    }

    @Override
    public void showItems(List<GodItem> movies, boolean hasMore) {
        Log.e("--->", "hit size" + movies.size());
        if (movies.size() == 0) {
            showNoDataView();
        } else {
            _QuickAdapter.addData(movies);

            if (hasMore) {
                _QuickAdapter.loadMoreComplete();
            } else {
                _QuickAdapter.loadMoreEnd();
            }
        }
    }

    private void showEmptyView() {
        _QuickAdapter.setNewData(null);
        _QuickAdapter.setEmptyView(R.layout.rv_empty_view);
    }

    private void showLoadingView() {
        _QuickAdapter.setNewData(null);
        _QuickAdapter.setEmptyView(R.layout.rv_loding_view);
    }

    private void showNoDataView() {
        _QuickAdapter.setNewData(null);
        _QuickAdapter.setEmptyView(R.layout.rv_nodata_view);
    }

    private void showErrorView() {
        _QuickAdapter.setNewData(null);
        _QuickAdapter.setEmptyView(R.layout.rv_error_view);
        _QuickAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeyWord(_lastKw);
            }
        });

        RxBus.getInstance().post(new Event_ShowNav());
    }

    String _lastKw;

    private void searchKeyWord(String keyWord) {
        _lastKw = keyWord;

        RxBus.getInstance().post(new Event_ShowNav());//显示底部栏
        Util.closeSoftKeyboard(_Ac); // 隐藏键盘

        if (!TextUtils.isEmpty(keyWord)) {
            showLoadingView();

            _P.cancelSearch();// 取消上一个搜索
            _P.searchByKW(keyWord);// go
        } else {
            showEmptyView();
        }
    }


    public class QuickAdapter_ZimuKu extends BaseQuickAdapter<IMovie, BaseViewHolder> {

        public QuickAdapter_ZimuKu() {
            super(R.layout.rv_item_movie);
        }

        public void addData(List<IMovie> movies) {
            if (mData == null) {
                mData = movies;
            } else {
                mData.addAll(movies);
            }
            notifyDataSetChanged();
        }

        @Override
        protected void convert(final BaseViewHolder helper, IMovie item) {

            Glide.with(SearchFragment.this)
                    .load(item.getPic_url())
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.img));

            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_alilas, item.getName_alias());
        }
    }

    public class QuickAdapter_Shooter extends BaseQuickAdapter<IZimu, BaseViewHolder> {

        public QuickAdapter_Shooter() {
            super(R.layout.rv_item_zimu_shooter);
        }

        public void addData(List<IZimu> zimuList) {
            if (mData == null) {
                mData = zimuList;
            } else {
                mData.addAll(zimuList);
            }
            notifyDataSetChanged();
        }

        @Override
        protected void convert(final BaseViewHolder helper, IZimu item) {
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_alilas, item.getAlias());
            helper.setText(R.id.tv_format, item.getFormat());
        }
    }


    private void showMenu() {

        String zimuku = ContantUtil.ZIMUKU;
        String shooter = ContantUtil.SHOOTER;

        if (_selectSource.equals(ContantUtil.ZIMUKU)) {
            zimuku += "√";
        } else if (_selectSource.equals(ContantUtil.SHOOTER)) {
            shooter += "√";
        }

        BottomSheetMenuDialog dialog = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem("选择数据源")
                .addItem(0, zimuku, R.drawable.ic_apps_black_24dp)
                .addItem(1, shooter, R.drawable.ic_apps_black_24dp)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        if (item.getTitle().equals(ContantUtil.ZIMUKU)) {
                            _selectSource = ContantUtil.ZIMUKU;
                            Prefs.with(App.get()).write(ContantUtil.SELECT_SOURCE, _selectSource);
                        } else if (item.getTitle().equals(ContantUtil.SHOOTER)) {
                            _selectSource = ContantUtil.SHOOTER;
                            Prefs.with(App.get()).write(ContantUtil.SELECT_SOURCE, ContantUtil.SHOOTER);
                        }
                        chooseOne();
                    }
                }).createDialog();
        dialog.show();
    }


}
