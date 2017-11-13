package com.linheimx.zimudog.vp.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.linheimx.lcustom.custom.utils.Util
import com.linheimx.lcustom.custom.view.SearchBar
import com.linheimx.lspider.god.GodItem
import com.linheimx.lspider.god.IMovie
import com.linheimx.lspider.god.IZimu
import com.linheimx.zimudog.App
import com.linheimx.zimudog.R
import com.linheimx.zimudog.m.bean.event.Event_ShowNav
import com.linheimx.zimudog.m.net.ApiManager2222
import com.linheimx.zimudog.m.net.download.DownloaderManager
import com.linheimx.zimudog.utils.ContantUtil
import com.linheimx.zimudog.utils.Utils
import com.linheimx.zimudog.utils.rxbus.RxBus
import com.linheimx.zimudog.vp.base.BaseFragment

import java.io.File

import butterknife.BindView
import com.linheimx.zimudog.m.bean.Movie
import com.linheimx.zimudog.utils.bindView
import es.dmoral.prefs.Prefs
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchFragment : BaseFragment(), IContract.V {

    lateinit var _QuickAdapter: QuickAdapter_ZimuKu
    lateinit var _P: IContract.P

    val searchBar: SearchBar by bindView(R.id.search_bar)
    val _rv: RecyclerView by bindView(R.id.rv)

    var _lastKw: String = ""

    override fun _ProvideLayout(): Int {
        return R.layout.fragment_search
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // search bar
        searchBar.setSearchClickListener(object : SearchBar.onSearchBarClickListener {
            override fun onSearchClick(searchContent: String) {
                searchKeyWord(searchContent)
            }

            override fun onMenuClick() {
                showMenu()
            }
        })

        // _rv
        _rv.setHasFixedSize(true)
        _rv.layoutManager = LinearLayoutManager(activity)
        showEmptyView()


        setAdapter4zimuku()
        _P = P_Zimuku(this)
    }

    private fun setAdapter4zimuku() {
        _QuickAdapter = QuickAdapter_ZimuKu()
        _QuickAdapter.bindToRecyclerView(_rv)
        _QuickAdapter.setOnLoadMoreListener({ _P!!.loadMore() }, _rv)
        _QuickAdapter.setOnItemClickListener { adapter, view, position ->
            val movieList = _QuickAdapter.data
            val movie = movieList[position]
            val zimuDialog = ZimuDialog.newInstance(movie)
            zimuDialog.show(childFragmentManager, null)
        }
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

    private fun showNoDataView() {
        _QuickAdapter.setNewData(null)
        _QuickAdapter.setEmptyView(R.layout.rv_nodata_view)
    }

    private fun showErrorView() {
        _QuickAdapter.setNewData(null)
        _QuickAdapter.setEmptyView(R.layout.rv_error_view)
        _QuickAdapter.emptyView.setOnClickListener { searchKeyWord(_lastKw) }

        RxBus.instance!!.post(Event_ShowNav())
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


    inner class QuickAdapter_ZimuKu : BaseQuickAdapter<IMovie<*>, BaseViewHolder>(R.layout.rv_item_movie) {

        override fun addData(movies: List<IMovie<*>>) {
            if (mData == null) {
                mData = movies
            } else {
                mData.addAll(movies)
            }
            notifyDataSetChanged()
        }

        override fun convert(helper: BaseViewHolder, item: IMovie<*>) {

            Glide.with(this@SearchFragment)
                    .load(item.pic_url)
                    .crossFade()
                    .into(helper.getView<View>(R.id.img) as ImageView)

            helper.setText(R.id.tv_name, item.name)
            helper.setText(R.id.tv_alilas, item.name_alias)
        }
    }

    inner class QuickAdapter_Shooter : BaseQuickAdapter<IZimu, BaseViewHolder>(R.layout.rv_item_zimu_shooter) {

        override fun addData(zimuList: List<IZimu>) {
            if (mData == null) {
                mData = zimuList
            } else {
                mData.addAll(zimuList)
            }
            notifyDataSetChanged()
        }

        override fun convert(helper: BaseViewHolder, item: IZimu?) {
            helper.setText(R.id.tv_name, item!!.name)
            helper.setText(R.id.tv_alilas, item.alias)
            helper.setText(R.id.tv_format, item.format)

            // 点击事件
            helper.itemView.setOnClickListener {
                if (!Utils.isNetConnected) {
                    Toasty.info(App.get()!!, "请检查您的网络！", Toast.LENGTH_SHORT, true).show()
                } else {
                    Toasty.success(activity, "已加入下载队列", Toast.LENGTH_SHORT, true).show()

                    ApiManager2222.instence
                            .getDownloadUrl4Shooter(item.download_page)
                            .observeOn(Schedulers.io())
                            .subscribe(object : Observer<String> {
                                override fun onSubscribe(d: Disposable) {}

                                override fun onNext(s: String) {
                                    if (item != null) {

                                        var fileName = item.name// 名字中可能包含 /
                                        fileName = fileName.replace('/', '_')
                                        val filePath = Utils.rootDirPath + "/" + fileName

                                        DownloaderManager
                                                .instance
                                                .startDownload(item.download_page, item, s, File(filePath))
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    //                                        Toasty.error(App.get(), "网络好像有点问题哦！", Toast.LENGTH_SHORT, true).show();
                                }

                                override fun onComplete() {

                                }

                            })

                }
            }
        }

    }

    private fun showMenu() {

        var zimuku = ContantUtil.ZIMUKU
        var shooter = ContantUtil.SHOOTER

        if (_selectSource == ContantUtil.ZIMUKU) {
            zimuku += "√"
        } else if (_selectSource == ContantUtil.SHOOTER) {
            shooter += "√"
        }

        val dialog = BottomSheetBuilder(activity)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem("选择数据源")
                .addItem(0, zimuku, R.drawable.ic_apps_black_24dp)
                .addItem(1, shooter, R.drawable.ic_apps_black_24dp)
                .setItemClickListener { item ->
                    if (item.title == ContantUtil.ZIMUKU) {
                        _selectSource = ContantUtil.ZIMUKU
                        Prefs.with(App.get()!!).write(ContantUtil.SELECT_SOURCE, _selectSource)
                    } else if (item.title == ContantUtil.SHOOTER) {
                        _selectSource = ContantUtil.SHOOTER
                        Prefs.with(App.get()!!).write(ContantUtil.SELECT_SOURCE, ContantUtil.SHOOTER)
                    }
                    chooseOne()
                }.createDialog()
        dialog.show()
    }

}// Required empty public constructor
