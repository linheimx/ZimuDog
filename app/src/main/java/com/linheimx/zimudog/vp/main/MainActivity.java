package com.linheimx.zimudog.vp.main;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.linheimx.zimudog.App;
import com.linheimx.zimudog.R;
import com.linheimx.zimudog.m.bean.event.Event_ShowNav;
import com.linheimx.zimudog.utils.Utils;
import com.linheimx.zimudog.utils.rxbus.RxBus;
import com.linheimx.zimudog.vp.about.AboutFragment;
import com.linheimx.zimudog.vp.browzimu.BrowZimuFragment;
import com.linheimx.zimudog.vp.search.SearchFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    public static final String F_SEARCH = "search";
    public static final String F_BROW = "brow";
    public static final String F_ABOUT = "about";

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            showFragment(F_SEARCH);
        }

        initBottomNav();

        requestPermission();
    }

    private void initBottomNav() {

        ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("搜索", R.drawable.ic_apps_black_24dp, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("查看", R.drawable.ic_maps_local_bar, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("关于", R.drawable.ic_maps_local_restaurant, R.color.color_tab_3);

        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);

        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.addItems(bottomNavigationItems);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        showFragment(F_SEARCH);
                        break;
                    case 1:
                        showFragment(F_BROW);
                        break;
                    case 2:
                        showFragment(F_ABOUT);
                        break;
                }
                return true;
            }
        });

        disposable = RxBus.getInstance().toFlowable(Event_ShowNav.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Event_ShowNav>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Event_ShowNav event_showNav) throws Exception {
                        bottomNavigation.restoreBottomNavigation(true);
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.dispose();
    }

    long _lastHitTime = System.currentTimeMillis() - 2001;

    @Override
    public void onBackPressed() {
        long nowHitTime = System.currentTimeMillis();
        if ((nowHitTime - _lastHitTime) <= 2000) {
            finish();
        } else {
            Toasty.info(App.get(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            _lastHitTime = nowHitTime;
        }
    }

    private void requestPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            // 未授予
                            Toasty.warning(App.get(), "读写权限未授予，字幕狗将不会正常运行，请授予其权限！", Toast.LENGTH_SHORT, true).show();
                            requestPermission();
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
