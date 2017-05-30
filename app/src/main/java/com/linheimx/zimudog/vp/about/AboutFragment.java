package com.linheimx.zimudog.vp.about;


import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.linheimx.zimudog.R;
import com.linheimx.zimudog.vp.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public int _ProvideLayout() {
        return R.layout.fragment_about;
    }

    @OnClick(R.id.btn_howto)
    void btnHowTo() {

        HowToDialog dialog = new HowToDialog();
        dialog.show(getChildFragmentManager(), null);
    }


}
