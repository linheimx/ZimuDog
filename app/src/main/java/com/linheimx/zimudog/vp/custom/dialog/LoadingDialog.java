package com.linheimx.zimudog.vp.custom.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linheimx.zimudog.R;

/**
 * Created by x1c on 2017/4/23.
 */

public class LoadingDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, null);
        return view;
    }

}
