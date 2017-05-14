package com.linheimx.zimudog.utils.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linheimx.lspider.zimuku.bean.Movie;
import com.linheimx.lspider.zimuku.bean.Zimu;
import com.linheimx.zimudog.App;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.net.ApiManager;
import com.linheimx.zimudog.m.net.download.DownloaderManager;
import com.linheimx.zimudog.utils.Utils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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

        public QuickAdapter() {
            super(R.layout.rv_item_zimu);
        }

        public void addData(List<Zimu> zimuList) {
            if (mData == null) {
                mData = zimuList;
            } else {
                mData.addAll(zimuList);
            }
            notifyDataSetChanged();
        }

        public void clearData() {
            setNewData(null);
            notifyDataSetChanged();
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Zimu item) {
            // logo
            String picUrl = item.getPic_url();
            if (picUrl.contains("jollyroger")) {
                Glide.with(ZimuDialog.this)
                        .load(R.drawable.doge1)
                        .crossFade()
                        .into((ImageView) helper.getView(R.id.img));
            } else {
                Glide.with(ZimuDialog.this)
                        .load(item.getPic_url())
                        .crossFade()
                        .into((ImageView) helper.getView(R.id.img));
            }


            // title
            helper.setText(R.id.tv_title, item.getName());

            // 点击事件
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!Utils.isNetConnected()) {
                        Toasty.info(App.get(), "请检查您的网络！", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.success(getActivity(), "已加入下载队列", Toast.LENGTH_SHORT, true).show();

                        ApiManager.getInstence()
                                .getDownloadUrlForZimu(item.getDownload_page())
                                .observeOn(Schedulers.io())
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        _CompositeDisposable.add(d);
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        if (_Dialog != null && _Dialog.isShowing()) {     // 检测dialog 是否在显示状态
                                            String filePath = Utils.getRootDirPath() + "/" + item.getName();

                                            DownloaderManager
                                                    .getInstance()
                                                    .startDownload(item.getDownload_page(), item, s, new File(filePath));
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toasty.error(App.get(), "网络好像有点问题哦！", Toast.LENGTH_SHORT, true).show();
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }
            });
        }
    }
}
