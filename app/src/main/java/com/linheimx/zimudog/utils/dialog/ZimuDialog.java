package com.linheimx.zimudog.utils.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linheimx.lcustom.custom.view.SnackProgressView;
import com.linheimx.lspider.zimuku.bean.Movie;
import com.linheimx.lspider.zimuku.bean.Zimu;
import com.linheimx.zimudog.App;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.net.ApiManager;
import com.linheimx.zimudog.m.net.download.DownloaderManager;
import com.linheimx.zimudog.m.net.download.ProgressEvent;
import com.linheimx.zimudog.utils.Utils;
import com.linheimx.zimudog.utils.rxbus.RxBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by x1c on 2017/4/23.
 */

public class ZimuDialog extends DialogFragment {


    @BindView(R.id.rv)
    RecyclerView rv;

    Dialog _Dialog;
    View _view;
    Unbinder unbinder;
    QuickAdapter _QuickAdapter;
    CompositeDisposable _CompositeDisposable;

    public static ZimuDialog newInstance(Movie movie) {
        ZimuDialog dialog = new ZimuDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", movie);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.dialog_zimu, null);
        _view.setY(-2500f);
        unbinder = ButterKnife.bind(this, _view);
        return _view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _CompositeDisposable = new CompositeDisposable();

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();
        Movie movie = (Movie) bundle.getSerializable("movie");
        _QuickAdapter = new QuickAdapter();
        _QuickAdapter.openLoadAnimation();
        _QuickAdapter.addData(movie.getList_zimu());
        rv.setAdapter(_QuickAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        // show the view
        _view.animate()
                .alpha(1)
                .y(0)
                .setDuration(680)
                .start();

        Dialog dialog = getDialog();
        if (dialog != null) {
            _Dialog = dialog;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        _CompositeDisposable.dispose();
    }


    public class QuickAdapter extends BaseQuickAdapter<Zimu, BaseViewHolder> {

        private static final int Progress_Start = 6;

        public QuickAdapter() {
            super(R.layout.rv_item_zimu);
        }

        public void addData(List<Zimu> movies) {
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
        protected void convert(final BaseViewHolder helper, final Zimu item) {

            /***********************************  进度条 ***********************************/
            final SnackProgressView progressView = helper.getView(R.id.progress);
            progressView.setMax(100);
            progressView.setProgress(0);
            Disposable oldDispose = (Disposable) progressView.getTag();
            if (oldDispose != null) {
                oldDispose.dispose();
                _CompositeDisposable.delete(oldDispose);
            }

            // 观察字幕的下载状态
            Disposable newDispose = RxBus.getInstance()
                    .toFlowable(ProgressEvent.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ProgressEvent>() {
                        @Override
                        public void accept(@NonNull ProgressEvent progressEvent) throws Exception {
                            // 进度条的更新
                            if (item.getDownload_page() != null) {
                                if (item.getDownload_page().equals(progressEvent.getUrlKey())) {
                                    float percent = (100 * progressEvent.getProgress()) / progressEvent.getMax();
                                    progressView.setProgress(percent > Progress_Start ? percent : Progress_Start);
                                }
                            }
                        }
                    });
            _CompositeDisposable.add(newDispose);
            progressView.setTag(newDispose);


            // logo
            Glide.with(ZimuDialog.this)
                    .load(item.getPic_url())
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.img));

            // title
            helper.setText(R.id.tv_title, item.getName());

            // 点击事件
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!Utils.isNetConnected()) {
                        Toasty.info(App.get(), "请检查您的网络！", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    Toasty.success(getActivity(), "已加入下载队列", Toast.LENGTH_SHORT, true).show();
                    // 提示进度：已经开始下载啦
                    if (progressView.getProgress() < Progress_Start) {
                        progressView.setProgress(Progress_Start);
                    }

                    ApiManager.getInstence()
                            .getDownloadUrlForZimu(item.getDownload_page())
                            .observeOn(Schedulers.io())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(@NonNull String downloadUrl) throws Exception {
                                    if (_Dialog != null && _Dialog.isShowing()) {     // 检测dialog 是否在显示状态

                                        String filePath = Utils.getRootDirPath() + "/" + item.getName();

                                        /**
                                         *  以下载的页面为key（因为下载的url会经常变化）
                                         */
                                        DownloaderManager
                                                .getInstance()
                                                .startDownload(item.getDownload_page(), downloadUrl, new File(filePath));
                                    }
                                }
                            });
                }
            });
        }
    }
}
