package com.linheimx.zimudog.vp.browzimu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.hu.p7zip.ZipUtils;
import com.linheimx.zimudog.App;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.net.download.event.EventZimuChanged;
import com.linheimx.zimudog.utils.Utils;
import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
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
    RecyclerView _rv;
    @BindView(R.id.tv_nav)
    TextView _tv_nav;
    @BindView(R.id.srl)
    SwipeRefreshLayout _srl;

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

        _rv.setHasFixedSize(true);
        _rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        _QuickAdapter = new QuickAdapter();
        _QuickAdapter.bindToRecyclerView(_rv);
        _QuickAdapter.openLoadAnimation();
        _QuickAdapter.setEmptyView(R.layout.rv_empty_view_sdcard);

        /****************************  观察正在下载的一堆字幕）  *****************************/
        Disposable disposable = RxBus_Behavior.getInstance()
                .toFlowable(EventZimuChanged.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EventZimuChanged>() {
                    @Override
                    public void accept(@NonNull EventZimuChanged eventZimuChanged) throws Exception {
                        _QuickAdapter.filesChanged();
                    }
                });
        _CompositeDisposable.add(disposable);

        _srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _rv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _QuickAdapter.rest2ZimuDog();
                        Toasty.info(App.get(), "定位到了 手机的内部存储/ZimuDog目录", Toast.LENGTH_LONG).show();
                        _srl.setRefreshing(false);
                    }
                }, 200);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        _CompositeDisposable.dispose();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    onNav();
                    return true;
                }
                return false;
            }
        });

        _QuickAdapter.filesChanged();
    }

    @OnClick(R.id.tv_nav)
    public void onNav() {
        File currentDir = _QuickAdapter.getCurrentDir();
        File parent = currentDir.getParentFile();
        if (parent != null) {
            // 安全检查
            if (parent.listFiles() == null) {
                return;
            }

            _QuickAdapter.setCurrentDir(parent);
            _QuickAdapter.filesChanged();
        }
    }


    public class QuickAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

        File currentDir;

        public QuickAdapter() {
            super(R.layout.rv_item_sdcard);

            rest2ZimuDog();
        }


        public void rest2ZimuDog() {
            File dir_zimu = new File(Utils.getRootDirPath());
            currentDir = dir_zimu;
            filesChanged();
        }

        public void filesChanged() {
            _tv_nav.setText(currentDir.getPath());
            File[] files = currentDir.listFiles();
            if (files != null && files.length > 0) { // check
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        long ret = o1.lastModified() - o2.lastModified();
                        if (ret > 0) {
                            return -1;
                        } else if (ret == 0) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
                mData = Arrays.asList(files);
            } else {
                mData = new ArrayList<>();
            }

            notifyDataSetChanged();
        }

        public File getCurrentDir() {
            return currentDir;
        }

        public void setCurrentDir(File currentDir) {
            this.currentDir = currentDir;
        }

        @Override
        protected void convert(final BaseViewHolder helper, final File file) {

            // type
            TextView tv = helper.getView(R.id.tv_type);
            if (file.isFile()) {
                tv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tv_file));
                String name = file.getName();
                try {
                    String[] types = name.split("\\.");
                    if (types != null && types.length > 0) {
                        helper.setText(R.id.tv_type, types[types.length - 1]);
                    }
                } catch (Exception e) {

                }
            } else {
                tv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tv_dir));
                tv.setText("");
            }

            // title
            helper.setText(R.id.tv_title, file.getName());

            // size
            String size = "";
            try {
                if (file.isFile()) {
                    size = convertFileSize(file.length());
                } else {
                    size = file.listFiles().length + "项";
                }
            } catch (Exception e) {
                
            }
            helper.setText(R.id.tv_size, size);

            // time
            Date currentTime = new Date(file.lastModified());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            helper.setText(R.id.tv_time, dateString + "");

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.isFile()) {
                        showMenu(file);
                    } else {
                        currentDir = file;
                        filesChanged();
                    }
                }
            });

            helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!file.isFile()) {
                        showMenu(file);
                    }
                    return true;
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
                            op_delete(file);
                        } else if (item.getTitle().equals("解压")) {
                            // check
                            if (file.getName().endsWith("zip") || file.getName().endsWith("rar")) {
                                op_unZipRar(file);
                            } else {
                                Toasty.info(App.get(), "不支持的解压格式", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).createDialog();
        dialog.show();
    }

    private void op_delete(File file) {
        deleFileRe(file);
        _QuickAdapter.filesChanged();
    }

    /**
     * 递归删除文件
     *
     * @param file
     */
    private void deleFileRe(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            for (File f : file.listFiles()) {
                deleFileRe(f);
            }
            if (file.listFiles().length == 0) {
                file.delete();
            }
        }
    }


    private void op_unZipRar(final File file) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setTitle("解压中...");
        progressDialog.show();
        Flowable
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        int ret = -1;
                        try {
                            Thread.sleep(1000);//延时一下

                            String parentPath = file.getParent();

                            /*****************************  重命名 *****************************/
                            // 考虑到文件名字的原因,重命名！
                            String originFileName = file.getName();
                            String newFileName = parentPath + "/" + System.currentTimeMillis();
                            File tmpFile = new File(newFileName);
                            file.renameTo(tmpFile);

                            // out dir
                            File tmpDir = new File(file.getParent() + "/d" + System.currentTimeMillis());
                            if (!tmpDir.exists()) {
                                tmpDir.mkdir();
                            } else {
                                deleFileRe(tmpDir);
                                tmpDir.mkdir();
                            }

                            // cmd
                            StringBuilder cmd = new StringBuilder("7z x");
                            cmd.append(" " + tmpFile.getPath());    //7z x 'aaa/bbb.zip'
                            cmd.append(" -o" + tmpDir.getPath() + "");    //7z x 'a.zip' '-o/out/'

                            ret = ZipUtils.executeCommand(cmd.toString());


                            /***************************  恢复名称  **************************/
                            File finalFile = new File(parentPath + "/" + originFileName);
                            finalFile.createNewFile();
                            tmpFile.renameTo(finalFile);

                            String dirName = originFileName.split("\\.")[0];
                            File finalDir = new File(parentPath + "/" + dirName);
                            if (finalDir.exists()) {
                                deleFileRe(finalDir);
                            }
                            finalDir.mkdir();
                            tmpDir.renameTo(finalDir);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ret == -1 ? false : true;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean o) throws Exception {
                        _QuickAdapter.filesChanged();
                        progressDialog.dismiss();
                        if (!o) {
                            Toasty.error(App.get(), "解压失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
