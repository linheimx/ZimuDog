package com.linheimx.zimudog.vp.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.linheimx.lcustom.custom.utils.Util
import com.linheimx.lcustom.custom.view.SearchBar
import com.linheimx.zimudog.R
import com.linheimx.zimudog.vp.base.BaseFragment
import com.linheimx.zimudog.m.bean.Movie
import com.linheimx.zimudog.utils.bindView

class SearchFragment : BaseFragment(), IContract.V {

    lateinit var _QuickAdapter: QuickAdapter_ZimuKu
    lateinit var _P: IContract.P

    val searchBar: SearchBar by bindView(R.id.search_bar)
    val _rv: RecyclerView by bindView(R.id.rv)

    var _lastKw: String = ""

    override fun _ProvideLayout(): Int = R.layout.fragment_search

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // search bar
        searchBar.setSearchClickListener(object : SearchBar.onSearchBarClickListener {
            override fun onSearchClick(searchContent: String) {
                searchKeyWord(searchContent)
            }

            override fun onMenuClick() {

            }
        })

        setAdapter4zimuku()
        _P = P_Zimuku(this)
    }

    private fun setAdapter4zimuku() {

        _QuickAdapter = QuickAdapter_ZimuKu()
        _QuickAdapter.bindToRecyclerView(_rv)
        _QuickAdapter.setOnLoadMoreListener({ _P.loadMore() }, _rv)
        _QuickAdapter.setOnItemClickListener { adapter, view, position ->
            //            val movieList = _QuickAdapter.data
//            val movie = movieList[position]
//            val zimuDialog = ZimuDialog.newInstance(movie)
//            zimuDialog.show(childFragmentManager, null)
        }

        _rv.setHasFixedSize(true)
        _rv.layoutManager = LinearLayoutManager(activity)
        showEmptyView()
    }

    override fun showLoadingError() {
        showErrorView()
    }

    override fun showItems(movies: List<Movie>, hasMore: Boolean) {
        Log.e("--->", "hit size" + movies.size)
        if (movies.size == 0) {
            showNoDataView()
        } else {
            _QuickAdapter.addData(movies)

            if (hasMore) {
                _QuickAdapter.loadMoreComplete()
            } else {
                _QuickAdapter.loadMoreEnd()
            }
        }
    }

    private fun showEmptyView() {
        _QuickAdapter.setNewData(null)
        _QuickAdapter.setEmptyView(R.layout.rv_empty_view)
    }

    private fun showLoadingView() {
        _QuickAdapter.setNewData(null)
        _QuickAdapter.setEmptyView(R.layout.rv_loding_view)
    }

    override fun showNoDataView() {
        _QuickAdapter.setNewData(null)
        _QuickAdapter.setEmptyView(R.layout.rv_nodata_view)
    }

    private fun showErrorView() {
        _QuickAdapter.setNewData(null)
        _QuickAdapter.setEmptyView(R.layout.rv_error_view)
        _QuickAdapter.emptyView.setOnClickListener { searchKeyWord(_lastKw) }
    }

    private fun searchKeyWord(keyWord: String) {
        _lastKw = keyWord

        Util.closeSoftKeyboard(activity) // 隐藏键盘

        if (!TextUtils.isEmpty(keyWord)) {
            showLoadingView()

            _P.cancelSearch()// 取消上一个搜索
            _P.searchByKW(keyWord)// go
        } else {
            showEmptyView()
        }
    }

    inner class QuickAdapter_ZimuKu : BaseQuickAdapter<Movie, BaseViewHolder>(R.layout.rv_item_movie) {

        override fun addData(movies: List<Movie>) {
            if (mData == null) {
                mData = movies
            } else {
                mData.addAll(movies)
            }
            notifyDataSetChanged()
        }

        override fun convert(helper: BaseViewHolder, item: Movie) {

            Glide.with(this@SearchFragment)
                    .load(item.avatar_url)
                    .crossFade()
                    .into(helper.getView<View>(R.id.img) as ImageView)

            helper.setText(R.id.tv_name, item.name)
            helper.setText(R.id.tv_alilas, item.name)
        }
    }
}
