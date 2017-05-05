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

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linheimx.lspider.zimuku.bean.Movie;
import com.linheimx.lspider.zimuku.bean.Zimu;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.net.ApiManager;
import com.linheimx.zimudog.m.net.download.Downloader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by x1c on 2017/4/23.
 */

public class ZimuDialog extends DialogFragment {


    @BindView(R.id.rv)
    RecyclerView rv;

    View _view;

    Unbinder unbinder;

    QuickAdapter _QuickAdapter;

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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public class QuickAdapter extends BaseQuickAdapter<Zimu, BaseViewHolder> {

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

            Glide.with(ZimuDialog.this)
                    .load(item.getPic_url())
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.img));

            helper.setText(R.id.tv_title, item.getName());

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiManager.getInstence()
                            .getDownloadUrlForZimu(item.getDownload_page())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(@NonNull String s) throws Exception {
                                    Downloader downloader = new Downloader(s);
                                    downloader.call();
                                }
                            });
                }
            });
        }
    }
}
