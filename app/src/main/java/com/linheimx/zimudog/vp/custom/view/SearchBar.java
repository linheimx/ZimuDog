package com.linheimx.zimudog.vp.custom.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.linheimx.zimudog.R;
import com.linheimx.zimudog.utils.Util;

/**
 * Created by x1c on 2017/4/24.
 */

public class SearchBar extends FrameLayout {

    View _searchView;
    ImageView _nav;
    SearchInputView _et;
    ImageView _search;

    DrawerArrowDrawable _arrowDrawable;

    onSearchClickListener _listener;

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

        _nav = (ImageView) _searchView.findViewById(R.id.iv_nav);
        _et = (SearchInputView) _searchView.findViewById(R.id.et_text);
        _search = (ImageView) _searchView.findViewById(R.id.search);

        _arrowDrawable = new DrawerArrowDrawable(context);
        _nav.setImageDrawable(_arrowDrawable);


        _et.setOnSearchKeyListener(new SearchInputView.OnKeyboardSearchKeyClickListener() {
            @Override
            public void onSearchKeyClicked() {
                if (_listener != null) {
                    _listener.onSearchClick(_et.getText().toString());
                }
            }
        });
        _search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_listener != null) {
                    _listener.onSearchClick(_et.getText().toString());
                }
            }
        });
    }

    public void setDrawerLayout(final DrawerLayout drawerLayout, final Activity activity) {
        // 汉堡包动画
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                _arrowDrawable.setProgress(slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Util.closeSoftKeyboard(activity);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        // 展开布局
        _nav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void setSearchClickListener(onSearchClickListener listener) {
        this._listener = listener;
    }

    public interface onSearchClickListener {
        void onSearchClick(String searchContent);
    }

}
