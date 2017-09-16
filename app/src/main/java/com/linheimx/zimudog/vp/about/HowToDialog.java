package com.linheimx.zimudog.vp.about;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linheimx.lspider.god.IMovie;
import com.linheimx.lspider.zimuku.bean.Zimu;
import com.linheimx.zimudog.App;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.net.ApiManager;
import com.linheimx.zimudog.m.net.download.DownloaderManager;
import com.linheimx.zimudog.utils.Utils;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.RichType;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by x1c on 2017/4/23.
 */

public class HowToDialog extends DialogFragment {


    @BindView(R.id.tv)
    TextView _tv;

    Dialog _Dialog;
    View _view;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.dialog_howto, null);
        _view.setY(-2500f);
        unbinder = ButterKnife.bind(this, _view);
        return _view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String c = Utils.getStringFromAssetFile(getActivity().getAssets(), "howto.md");
        RichText.fromMarkdown(c).into(_tv);
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
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.father)
    void hide() {
        _Dialog.dismiss();
    }
}
