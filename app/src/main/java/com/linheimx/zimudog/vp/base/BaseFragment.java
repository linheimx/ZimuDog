package com.linheimx.zimudog.vp.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by ari on 8/16/16.
 */
public abstract class BaseFragment extends Fragment {


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public abstract boolean onActivityBackPress();
}
