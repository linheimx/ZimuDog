//package com.linheimx.zimudog.vp.search
//
//import android.app.Dialog
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.support.v4.app.DialogFragment
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.text.TextUtils
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.Toast
//
//import com.bumptech.glide.Glide
//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.BaseViewHolder
//import com.linheimx.lspider.god.IMovie
//import com.linheimx.lspider.zimuku.bean.Zimu
//import com.linheimx.zimudog.App
//import com.linheimx.zimudog.R
//import com.linheimx.zimudog.m.net.ApiManager2222
//import com.linheimx.zimudog.m.net.download.DownloaderManager
//import com.linheimx.zimudog.utils.Utils
//
//import java.io.File
//import java.util.concurrent.TimeUnit
//
//import butterknife.BindView
//import butterknife.ButterKnife
//import butterknife.Unbinder
//import es.dmoral.toasty.Toasty
//import io.reactivex.Observer
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.CompositeDisposable
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//
//
///**
// * Created by x1c on 2017/4/23.
// */
//
//class ZimuDialog : DialogFragment() {
//
//
//    @BindView(R.id.rv)
//    internal var rv: RecyclerView? = null
//
//    internal var _Dialog: Dialog
//    internal var _view: View
//    internal var unbinder: Unbinder
//    internal var _QuickAdapter: QuickAdapter
//    internal var _CompositeDisposable: CompositeDisposable
//
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        _view = inflater!!.inflate(R.layout.dialog_zimu, null)
//        _view.y = -2500f
//        unbinder = ButterKnife.bind(this, _view)
//        return _view
//    }
//
//    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        _CompositeDisposable = CompositeDisposable()
//
//        rv!!.setHasFixedSize(true)
//        rv!!.layoutManager = LinearLayoutManager(activity)
//
//        val bundle = arguments
//        val movie = bundle.getSerializable("movie") as IMovie<*>
//        _QuickAdapter = QuickAdapter()
//        _QuickAdapter.bindToRecyclerView(rv)
//        _QuickAdapter.setEmptyView(R.layout.rv_loding_view2)
//
//        val allZimuLink = movie.allZimuLink
//        if (!TextUtils.isEmpty(allZimuLink)) {
//            ApiManager2222.instence.getAllZimuFromUrl(allZimuLink)
//                    .delay(1000, TimeUnit.MILLISECONDS)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(object : Observer<List<Zimu>> {
//                        override fun onSubscribe(d: Disposable) {
//                            _CompositeDisposable.add(d)
//                        }
//
//                        override fun onNext(zimus: List<Zimu>) {
//                            _QuickAdapter.setNewData(zimus)
//                        }
//
//                        override fun onError(e: Throwable) {
//                            _QuickAdapter.setEmptyView(R.layout.rv_error_view)
//                        }
//
//                        override fun onComplete() {
//
//                        }
//                    })
//        } else {
//            _QuickAdapter.addData(movie.top5Zimus)
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        // show the view
//        _view.animate()
//                .alpha(1f)
//                .y(0f)
//                .setDuration(680)
//                .start()
//
//        val dialog = dialog
//        if (dialog != null) {
//            _Dialog = dialog
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        unbinder.unbind()
//        _CompositeDisposable.dispose()
//    }
//
//
//    inner class QuickAdapter : BaseQuickAdapter<Zimu, BaseViewHolder>(R.layout.rv_item_zimu) {
//
//        override fun addData(zimuList: List<Zimu>) {
//            if (mData == null) {
//                mData = zimuList
//            } else {
//                mData.addAll(zimuList)
//            }
//            notifyDataSetChanged()
//        }
//
//        override fun convert(helper: BaseViewHolder, item: Zimu?) {
//            // logo
//            val picUrl = item!!.pic_url
//            if (picUrl.contains("jollyroger")) {
//                Glide.with(this@ZimuDialog)
//                        .load(R.drawable.doge_512_512)
//                        .crossFade()
//                        .into(helper.getView<View>(R.id.img) as ImageView)
//            } else {
//                Glide.with(this@ZimuDialog)
//                        .load(item.pic_url)
//                        .crossFade()
//                        .into(helper.getView<View>(R.id.img) as ImageView)
//            }
//
//
//            // title
//            helper.setText(R.id.tv_name, item.name)
//
//            // 点击事件
//            helper.itemView.setOnClickListener {
//                if (!Utils.isNetConnected) {
//                    Toasty.info(App.get()!!, "请检查您的网络！", Toast.LENGTH_SHORT, true).show()
//                } else {
//                    Toasty.success(activity, "已加入下载队列", Toast.LENGTH_SHORT, true).show()
//
//                    ApiManager2222.instence
//                            .getDownloadUrl4Zimu(item.download_page)
//                            .observeOn(Schedulers.io())
//                            .subscribe(object : Observer<String> {
//                                override fun onSubscribe(d: Disposable) {}
//
//                                override fun onNext(s: String) {
//                                    if (item != null) {
//                                        var fileName = item.name// 名字中可能包含 /
//                                        fileName = fileName.replace('/', '_')
//                                        val filePath = Utils.rootDirPath + "/" + fileName
//
//                                        Log.e("--->", "url:" + s)
//
//                                        DownloaderManager
//                                                .instance
//                                                .startDownload(item.download_page, item, s, File(filePath))
//                                    }
//                                }
//
//                                override fun onError(e: Throwable) {
//                                    //                                        Toasty.error(App.get(), "网络好像有点问题哦！", Toast.LENGTH_SHORT, true).show();
//                                }
//
//                                override fun onComplete() {
//
//                                }
//                            })
//                }
//            }
//        }
//    }
//
//    companion object {
//
//        fun newInstance(movie: IMovie<*>): ZimuDialog {
//            val dialog = ZimuDialog()
//            val bundle = Bundle()
//            bundle.putSerializable("movie", movie)
//            dialog.arguments = bundle
//            return dialog
//        }
//    }
//}
