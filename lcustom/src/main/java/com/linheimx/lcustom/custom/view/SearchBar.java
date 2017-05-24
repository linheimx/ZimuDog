package com.linheimx.lcustom.custom.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.linheimx.lcustom.R;

/**
 * Created by x1c on 2017/4/24.
 */

public class SearchBar extends FrameLayout {

    View _searchView;
    ImageView _nav;
    SearchInputView _et;
    ImageView _search;

    DrawerArrowDrawable _arrowDrawable;

    onSearchBarClickListener _listener;

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

        _nav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_listener != null) {
                    _listener.onMenuClick();
                }
            }
        });

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

    public void setSearchClickListener(onSearchBarClickListener listener) {
        this._listener = listener;
    }

    public interface onSearchBarClickListener {
        void onSearchClick(String searchContent);

        void onMenuClick();
    }

}
