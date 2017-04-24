package com.linheimx.zimudog.vp.custom.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.linheimx.zimudog.R;

/**
 * Created by x1c on 2017/4/24.
 */

public class SearchBar extends FrameLayout {

    View _searchView;

    public SearchBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        _searchView = inflate(context, R.layout.view_search_bar, this);
    }

}
