package com.linheimx.zimudog.vp.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.linheimx.zimudog.App;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.utils.Utils;
import com.linheimx.zimudog.vp.base.BaseFragment;
import com.linheimx.zimudog.vp.base.Provider;
import com.linheimx.zimudog.vp.browzimu.BrowZimuFragment;
import com.linheimx.zimudog.vp.search.SearchFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        Provider {

    public static final String F_SEARCH = "search";
    public static final String F_BROW = "about";

    @BindView(R.id.drawer_layout)
    DrawerLayout _DrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView _NavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        _NavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            showFragment(F_SEARCH);
        }

        requestPermission();
    }

    @Override
    public void onBackPressed() {
        List fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            BaseFragment currentFragment = (BaseFragment) fragments.get(fragments.size() - 1);
            if (!currentFragment._OnActivityBackPress()) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        _DrawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.navigation_search:
                _DrawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showFragment(F_SEARCH);
                    }
                }, 400);
                return true;
            case R.id.navigation_brow:
                _DrawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showFragment(F_BROW);
                    }
                }, 400);
                return true;
            case R.id.navigation_notifications:
                _DrawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showFragment(F_BROW);
                    }
                }, 400);
                return true;
            default:
                return true;
        }
    }

    @Override
    public DrawerLayout provideDrawLayout() {
        return _DrawerLayout;
    }

    private void requestPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            // 未授予
                            Toasty.warning(App.get(), "读写权限未授予，字幕狗将不会正常运行，请授予其权限！", Toast.LENGTH_SHORT, true).show();
                        } else {
                            Utils.mkRootDir();
                        }
                    }
                });
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
                case F_BROW:
                    fragment = new BrowZimuFragment();
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
