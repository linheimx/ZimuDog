package com.linheimx.zimudog.vp.browzimu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.hu.p7zip.ZipUtils;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.net.download.event.EventZimuChanged;
import com.linheimx.zimudog.utils.Utils;
import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.linheimx.zimudog.utils.Utils.convertFileSize;

/**
 * Created by LJIAN on 2017/5/9.
 */

public class SdCardFragment extends TitleFragment {

    @BindView(R.id.rv)
    RecyclerView rv;

    Unbinder unbinder;
    QuickAdapter _QuickAdapter;
    CompositeDisposable _CompositeDisposable;

    @Override
    public String getTitle() {
        return "已下载";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sdcard, null);
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
                        _QuickAdapter.dataChanged();
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


    public class QuickAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

        public QuickAdapter() {
            super(R.layout.rv_item_sdcard);
            dataChanged();
        }

        public void dataChanged() {
            File dir_zimu = new File(Utils.getRootDirPath());
            mData = Arrays.asList(dir_zimu.listFiles());
            notifyDataSetChanged();
        }

        @Override
        protected void convert(final BaseViewHolder helper, final File file) {

            // type
            String name = file.getName();
            String[] types = name.split("\\.");
            if (types != null && types.length > 0) {
                helper.setText(R.id.tv_type, types[types.length - 1]);
            }

            // title
            helper.setText(R.id.tv_title, file.getName());

            // size
            helper.setText(R.id.tv_size, convertFileSize(file.length()));

            // time
            Date currentTime = new Date(file.lastModified());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            helper.setText(R.id.tv_time, dateString + "");

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu(file);
                }
            });
        }
    }

    ProgressDialog progressDialog;

    private void showMenu(final File file) {
        BottomSheetMenuDialog dialog = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem("选择操作")
                .addItem(0, "删除", R.drawable.op_delete)
                .addItem(1, "解压", R.drawable.op_open)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        if (item.getTitle().equals("删除")) {
                            file.delete();
                            _QuickAdapter.dataChanged();
                        } else if (item.getTitle().equals("解压")) {

                            if (progressDialog == null) {
                                progressDialog = new ProgressDialog(getActivity());
                            }
                            progressDialog.setTitle("解压中...");
                            progressDialog.show();
                            Flowable
                                    .fromCallable(new Callable<Object>() {
                                        @Override
                                        public Object call() throws Exception {

                                            // type
                                            String name = file.getName();



                                            File outPath = new File(file.getPath());
                                            if (!outPath.exists()) {
                                                outPath.mkdir();
                                            } else {
                                                if (outPath.isFile()) {
                                                    outPath.mkdir();
                                                }
                                            }

                                            StringBuilder sbCmd = new StringBuilder("7z x");
                                            //input file path
                                            sbCmd.append(" " + file.getPath());    //7z x 'aaa/bbb.zip'
                                            //output path
                                            sbCmd.append(" -o" + outPath.getPath() + "");    //7z x 'a.zip' '-o/out/'

                                            int ret = ZipUtils.executeCommand(sbCmd.toString());

                                            return ret;
                                        }
                                    }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Object>() {
                                        @Override
                                        public void accept(@NonNull Object o) throws Exception {
                                            Log.e("--->", "2" + o);
                                            progressDialog.dismiss();
                                            _QuickAdapter.dataChanged();
                                        }
                                    });

                        }
                    }
                }).createDialog();
        dialog.show();
    }


}
