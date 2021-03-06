package com.linheimx.zimudog.vp.search

import android.app.Dialog
import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.NotificationCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.linheimx.zimudog.App
import com.linheimx.zimudog.R
import com.linheimx.zimudog.m.bean.*
import com.linheimx.zimudog.m.net.ApiManger
import com.linheimx.zimudog.utils.Utils
import java.util.concurrent.TimeUnit
import com.linheimx.zimudog.utils.bindView
import com.linheimx.zimudog.utils.rxbus.RxBus
import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior
import com.linheimx.zimudog.vp.main.MainActivity
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.model.FileDownloadStatus
import com.liulishuo.filedownloader.notification.BaseNotificationItem
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper
import com.liulishuo.filedownloader.notification.FileDownloadNotificationListener
import com.liulishuo.filedownloader.util.FileDownloadHelper
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by x1c on 2017/4/23.
 */

class ZimuDialog : DialogFragment() {

    val rv: RecyclerView by bindView(R.id.rv)

    lateinit var _Dialog: Dialog
    lateinit var _view: View
    lateinit var _QuickAdapter: QuickAdapter
    lateinit var _CompositeDisposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _view = inflater!!.inflate(R.layout.dialog_zimu, null)
        _view.y = -2500f
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _CompositeDisposable = CompositeDisposable()

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(activity)

        val bundle = arguments
        val movie = bundle!!.getSerializable("movie") as Movie
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
                    Toasty.success(App.get()!!, "已加入下载队列", Toast.LENGTH_SHORT, true).show()

                    ApiManger.api.getHtml(item.detail_url)
                            .subscribeOn(Schedulers.io())
                            .flatMap { ApiManger.apiParse.parse_Zimus_DURL(it.string()) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<Resp_Zimus_DURL> {
                                override fun onSubscribe(d: Disposable) {
                                    _CompositeDisposable.add(d)
                                }

                                override fun onNext(t: Resp_Zimus_DURL) {
                                    if (t.success) {
                                        var fileName = item.name// 名字中可能包含 /
                                        fileName = fileName.replace('/', '_')
                                        val filePath = Utils.rootDirPath + "/" + fileName

                                        download(t.obj!!, filePath)
                                    } else {
                                        Toasty.error(App.get()!!, "下载失败：" + t.errorMsg, Toast.LENGTH_SHORT, true).show()
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    _QuickAdapter.setEmptyView(R.layout.rv_error_view)
                                }

                                override fun onComplete() {
                                }
                            })
                }
            }
        }
    }

    fun download(url: String, path: String) {
        FileDownloader.getImpl().create(url)
                .setPath(path)
                .setAutoRetryTimes(2)
                .setListener(NotificationListener(FileDownloadNotificationHelper<NotificationItem>()))
                .start()
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


class NotificationListener(helper: FileDownloadNotificationHelper<*>) : FileDownloadNotificationListener(helper) {

    override fun create(task: BaseDownloadTask): BaseNotificationItem {
        return NotificationItem(task.id,
                task.filename, "(゜-゜)つロ")
    }

    override fun completed(task: BaseDownloadTask?) {
        super.completed(task)
        RxBus_Behavior.post(Ok())
    }
}

class NotificationItem constructor(id: Int, title: String, desc: String) : BaseNotificationItem(id, title, desc) {

    var pendingIntent: PendingIntent
    var builder: NotificationCompat.Builder

    init {
        val intents = arrayOfNulls<Intent>(1)
        intents[0] = Intent(App.get(), MainActivity::class.java)

        this.pendingIntent = PendingIntent.getActivities(App.get(), 0, intents,
                PendingIntent.FLAG_UPDATE_CURRENT)

        builder = NotificationCompat.Builder(FileDownloadHelper.getAppContext())

        builder.setDefaults(Notification.DEFAULT_LIGHTS)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentTitle(getTitle())
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.doge1)
    }

    override fun show(statusChanged: Boolean, status: Int, isShowProgress: Boolean) {

        var desc = desc

        builder.setContentTitle(title)
                .setContentText(desc)

        if (statusChanged) {
            builder.setTicker(desc)
        }

        builder.setProgress(total, sofar, !isShowProgress)
        manager.notify(id, builder.build())
    }
}