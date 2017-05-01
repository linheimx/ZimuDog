package com.linheimx.zimudog.vp.custom.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    DialogInterface.OnCancelListener onCancelListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Dialog dialog = getDialog();
        if (dialog != null) {
            if (onCancelListener != null) {
                dialog.setOnCancelListener(onCancelListener);
            }
        }
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;

        Dialog dialog = getDialog();
        if (dialog != null) {
            if (onCancelListener != null) {
                dialog.setOnCancelListener(onCancelListener);
            }
        }
    }
}
