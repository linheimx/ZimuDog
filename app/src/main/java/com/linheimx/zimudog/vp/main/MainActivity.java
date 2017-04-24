package com.linheimx.zimudog.vp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.linheimx.zimudog.R;
import com.linheimx.zimudog.utils.Util;
import com.linheimx.zimudog.vp.about.AboutFragment;
import com.linheimx.zimudog.vp.base.BaseFragment;
import com.linheimx.zimudog.vp.search.SearchFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public static final String F_SEARCH = "search";
    public static final String F_ABOUT = "about";

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(F_SEARCH);
                    return true;
                case R.id.navigation_dashboard:
                    showFragment(F_ABOUT);
                    return true;
                case R.id.navigation_notifications:
                    showFragment(F_SEARCH);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            showFragment(F_SEARCH);
        }
    }

    @Override
    public void onBackPressed() {
        List fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            BaseFragment currentFragment = (BaseFragment) fragments.get(fragments.size() - 1);
            if (!currentFragment.onActivityBackPress()) {
                super.onBackPressed();
            }
        }
    }


    public void showFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // hidden all
        if (fragmentManager.getFragments() != null) {
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (!fragment.getTag().equals(tag)) {
                    fragmentManager.beginTransaction()
                            .hide(fragment)
                            .commit();
                }
            }
        }


        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            switch (tag) {
                case F_SEARCH:
                    fragment = new SearchFragment();
                    break;
                case F_ABOUT:
                    fragment = new AboutFragment();
                    break;
            }
            fragmentManager.beginTransaction()
                    .add(R.id.content, fragment, tag)
                    .commit();
        } else {
            if (fragment.isHidden()) {
                fragmentManager.beginTransaction()
                        .show(fragment)
                        .commit();
            }
        }
    }
}
