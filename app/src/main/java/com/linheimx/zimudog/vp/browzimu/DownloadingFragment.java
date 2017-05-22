package com.linheimx.zimudog.vp.browzimu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linheimx.lcustom.custom.view.SnackProgressView;
import com.linheimx.lspider.zimuku.bean.Zimu;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.net.download.Downloader;
import com.linheimx.zimudog.m.net.download.event.EventZimuChanged;
import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by LJIAN on 2017/5/9.
 */

public class DownloadingFragment extends TitleFragment {

    @BindView(R.id.rv)
    RecyclerView rv;

    Unbinder unbinder;

    CompositeDisposable _CompositeDisposable;
    QuickAdapter _QuickAdapter;

    @Override
    public String getTitle() {
        return "正在下载";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloading, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _CompositeDisposable = new CompositeDisposable();

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        _QuickAdapter = new QuickAdapter();
        _QuickAdapter.openLoadAnimation();
        rv.setAdapter(_QuickAdapter);

        /****************************  观察正在下载的一堆字幕）  *****************************/
        Disposable disposable = RxBus_Behavior.getInstance()
                .toFlowable(EventZimuChanged.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EventZimuChanged>() {
                    @Override
                    public void accept(@NonNull EventZimuChanged eventZimuChanged) throws Exception {
                        _QuickAdapter.setNewData(eventZimuChanged.getZimuList());
                    }
                });

        _CompositeDisposable.add(disposable);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        _CompositeDisposable.dispose();
    }


    public class QuickAdapter extends BaseQuickAdapter<Zimu, BaseViewHolder> {

        public QuickAdapter() {
            super(R.layout.rv_item_zimu_download);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Zimu item) {

            /***********************************  进度条 ***********************************/
            final SnackProgressView progressView = helper.getView(R.id.progress);
            progressView.setMax(100);
            progressView.setProgress(0);
            Disposable oldDispose = (Disposable) progressView.getTag();
            if (oldDispose != null) {
                // 去掉旧的订阅
                oldDispose.dispose();
                _CompositeDisposable.delete(oldDispose);
            }

            // 观察字幕的下载状态
            Disposable newDispose = RxBus_Behavior.getInstance()
                    .toFlowable(Downloader.State.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Downloader.State>() {
                        @Override
                        public void accept(@NonNull Downloader.State state) throws Exception {
                            if (item.getDownload_page() != null) {
                                if (item.getDownload_page().equals(state.getKey())) {

                                    // 进度条的更新
                                    float percent = (100 * state.get_nowRead()) / state.get_nowAll();
                                    progressView.setProgress(percent);

                                    // 考虑出错的情况
                                    if (state.isError()) {
                                        progressView.setColor(Color.RED);
                                    }
                                }
                            }
                        }
                    });
            _CompositeDisposable.add(newDispose);
            progressView.setTag(newDispose);

            // title
            helper.setText(R.id.tv_name, item.getName());
        }
    }


}
