package com.linheimx.zimudog.vp.search

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.linheimx.zimudog.App
import com.linheimx.zimudog.R
import com.linheimx.zimudog.m.bean.Movie
import com.linheimx.zimudog.m.bean.Resp_Zimus
import com.linheimx.zimudog.m.bean.Resp_Zimus_DURL
import com.linheimx.zimudog.m.bean.Zimu
import com.linheimx.zimudog.m.net.ApiManger
import com.linheimx.zimudog.utils.Utils
import java.util.concurrent.TimeUnit
import com.linheimx.zimudog.utils.bindView
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import zlc.season.rxdownload3.RxDownload
import zlc.season.rxdownload3.core.Mission


/**
 * Created by x1c on 2017/4/23.
 */

class ZimuDialog : DialogFragment() {

    val rv: RecyclerView by bindView(R.id.rv)

    lateinit var _Dialog: Dialog
    lateinit var _view: View
    lateinit var _QuickAdapter: QuickAdapter
    lateinit var _CompositeDisposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view = inflater!!.inflate(R.layout.dialog_zimu, null)
        _view.y = -2500f
        return _view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _CompositeDisposable = CompositeDisposable()

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(activity)

        val bundle = arguments
        val movie = bundle.getSerializable("movie") as Movie
        _QuickAdapter = QuickAdapter()
        _QuickAdapter.bindToRecyclerView(rv)
        _QuickAdapter.setEmptyView(R.layout.rv_loding_view2)

        ApiManger.api.getHtml(movie.detail_url)
                .subscribeOn(Schedulers.io())
                .delay(1000, TimeUnit.MILLISECONDS)
                .flatMap { ApiManger.apiParse.parse_Zimus(it.string()) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Resp_Zimus> {
                    override fun onSubscribe(d: Disposable) {
                        _CompositeDisposable.add(d)
                    }

                    override fun onNext(t: Resp_Zimus) {
                        _QuickAdapter.setNewData(t.obj)
                    }

                    override fun onError(e: Throwable) {
                        _QuickAdapter.setEmptyView(R.layout.rv_error_view)
                    }

                    override fun onComplete() {
                    }
                })
    }

    override fun onResume() {
        super.onResume()

        // show the view
        _view.animate()
                .alpha(1f)
                .y(0f)
                .setDuration(680)
                .start()

        val dialog = dialog
        if (dialog != null) {
            _Dialog = dialog
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _CompositeDisposable.dispose()
    }

    inner class QuickAdapter : BaseQuickAdapter<Zimu, BaseViewHolder>(R.layout.rv_item_zimu) {

        override fun addData(zimuList: List<Zimu>) {
            if (mData == null) {
                mData = zimuList
            } else {
                mData.addAll(zimuList)
            }
            notifyDataSetChanged()
        }

        override fun convert(helper: BaseViewHolder, item: Zimu) {
            // logo
            val picUrl = item.avatar_url
            if (picUrl.contains("jollyroger")) {
                Glide.with(this@ZimuDialog)
                        .load(R.drawable.doge_512_512)
                        .crossFade()
                        .into(helper.getView<View>(R.id.img) as ImageView)
            } else {
                Glide.with(this@ZimuDialog)
                        .load(item.avatar_url)
                        .crossFade()
                        .into(helper.getView<View>(R.id.img) as ImageView)
            }

            // title
            helper.setText(R.id.tv_name, item.name)

            // 点击事件
            helper.itemView.setOnClickListener {
                if (!Utils.isNetConnected) {
                    Toasty.info(App.get()!!, "请检查您的网络！", Toast.LENGTH_SHORT, true).show()
                } else {
                    Toasty.success(activity, "已加入下载队列", Toast.LENGTH_SHORT, true).show()

                    ApiManger.api.getHtml(item.detail_url)
                            .subscribeOn(Schedulers.io())
                            .flatMap { ApiManger.apiParse.parse_Zimus_DURL(it.string()) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<Resp_Zimus_DURL> {
                                override fun onSubscribe(d: Disposable) {
                                    _CompositeDisposable.add(d)
                                }

                                override fun onNext(t: Resp_Zimus_DURL) {
                                    Log.e("===>", "" + t)

                                    var fileName = item.name// 名字中可能包含 /
                                    fileName = fileName.replace('/', '_')
                                    val filePath = Utils.rootDirPath + "/" + fileName

                                    RxDownload
                                            .create(Mission(t.obj!!, fileName, filePath))
                                            .subscribeOn(Schedulers.io()).subscribe()
                                }

                                override fun onError(e: Throwable) {
                                    _QuickAdapter.setEmptyView(R.layout.rv_error_view)
                                }

                                override fun onComplete() {
                                }
                            })

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
                }
            }
        }
    }

    companion object {

        fun newInstance(movie: Movie): ZimuDialog {
            val dialog = ZimuDialog()
            val bundle = Bundle()
            bundle.putSerializable("movie", movie)
            dialog.arguments = bundle
            return dialog
        }
    }
}
