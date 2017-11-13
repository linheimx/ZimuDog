package com.linheimx.zimudog.vp.browzimu

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.linheimx.zimudog.R
import com.linheimx.zimudog.utils.bindView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by LJIAN on 2017/5/9.
 */

class DownloadingFragment : TitleFragment() {

    val rv: RecyclerView by bindView(R.id.rv)
//    lateinit var _QuickAdapter: QuickAdapter

    override val title: String
        get() = "正在下载"

    override fun _ProvideLayout(): Int = R.layout.fragment_downloading

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _CompositeDisposable = CompositeDisposable()

        rv!!.setHasFixedSize(true)
        rv!!.layoutManager = LinearLayoutManager(activity)

//        _QuickAdapter = QuickAdapter()
//        _QuickAdapter.openLoadAnimation()
//        rv!!.adapter = _QuickAdapter
//
//        /****************************  观察正在下载的一堆字幕）   */
//        val disposable = RxBus_Behavior.instance!!
//                .toFlowable(EventZimuChanged::class.java)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(Consumer<EventZimuChanged> { eventZimuChanged -> _QuickAdapter.setNewData(eventZimuChanged.zimuList) })
//
//        _CompositeDisposable.add(disposable)
    }

//    inner class QuickAdapter : BaseQuickAdapter<IZimu, BaseViewHolder>(R.layout.rv_item_zimu_download) {
//
//        override fun convert(helper: BaseViewHolder, item: IZimu) {
//
//            /***********************************  进度条  */
//            val progressView = helper.getView<SnackProgressView>(R.id.progress)
//            progressView.setMax(100f)
//            progressView.progress = 0f
//            val oldDispose = progressView.tag as Disposable
//            if (oldDispose != null) {
//                // 去掉旧的订阅
//                oldDispose.dispose()
//                _CompositeDisposable.delete(oldDispose)
//            }
//
//            // 观察字幕的下载状态
//            val newDispose = RxBus_Behavior.instance!!
//                    .toFlowable(Downloader.State::class.java)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(Consumer<Downloader.State> { state ->
//                        if (item.download_page != null) {
//                            if (item.download_page == state.key) {
//
//                                // 进度条的更新
//                                val percent = (100 * state._nowRead / state._nowAll).toFloat()
//                                progressView.progress = percent
//
//                                // 考虑出错的情况
//                                if (state.isError) {
//                                    progressView.setColor(Color.RED)
//                                }
//                            }
//                        }
//                    })
//            _CompositeDisposable.add(newDispose)
//            progressView.tag = newDispose
//
//            // title
//            helper.setText(R.id.tv_name, item.name)
//        }
//    }


}
