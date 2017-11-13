package com.linheimx.zimudog.vp.main

import android.Manifest
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Toast
import com.linheimx.zimudog.App
import com.linheimx.zimudog.R
import com.linheimx.zimudog.utils.Utils
import com.linheimx.zimudog.vp.about.AboutFragment
import com.linheimx.zimudog.vp.browzimu.BrowZimuFragment
import com.linheimx.zimudog.vp.search.SearchFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import com.linheimx.zimudog.utils.bindView
import es.dmoral.toasty.Toasty
import io.reactivex.functions.Consumer


class MainActivity : AppCompatActivity() {

    val bottomNavigation: BottomNavigationView by bindView(R.id.bottom_navigation)

    internal var _lastHitTime = System.currentTimeMillis() - 2001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            showFragment(F_SEARCH)
        }

        initBottomNav()

        requestPermission()
    }

    private fun initBottomNav() {


        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_search -> showFragment(F_SEARCH)
                R.id.navigation_brow -> showFragment(F_BROW)
                R.id.navigation_notifications -> showFragment(F_ABOUT)
            }
            true
        }
    }

    override fun onBackPressed() {
        val nowHitTime = System.currentTimeMillis()
        if (nowHitTime - _lastHitTime <= 2000) {
            finish()
        } else {
            Toasty.info(App.get()!!, "再按一次返回键退出", Toast.LENGTH_SHORT).show()
            _lastHitTime = nowHitTime
        }
    }

    private fun requestPermission() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(Consumer {
                    if ((!it)) {
                        // 未授予
                        Toasty.warning(App.get()!!, "读写权限未授予，字幕狗将不会正常运行，请授予其权限！", Toast.LENGTH_SHORT, true).show()
                        requestPermission()
                    } else {
                        Utils.mkRootDir()
                    }
                })
    }


    fun showFragment(tag: String) {
        val fragmentManager = supportFragmentManager

        // hidden all
        if (fragmentManager.fragments != null) {
            for (fragment in fragmentManager.fragments) {
                if (fragment.tag != tag) {
                    fragmentManager.beginTransaction()
                            .hide(fragment)
                            .commit()
                }
            }
        }


        var fragment: Fragment? = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            when (tag) {
                F_SEARCH -> fragment = SearchFragment()
                F_BROW -> fragment = BrowZimuFragment()
                F_ABOUT -> fragment = AboutFragment()
            }
            fragmentManager.beginTransaction()
                    .add(R.id.content, fragment, tag)
                    .commit()
        } else {
            if (fragment.isHidden) {
                fragmentManager.beginTransaction()
                        .show(fragment)
                        .commit()
            }
        }
    }

    companion object {

        val F_SEARCH = "search"
        val F_BROW = "brow"
        val F_ABOUT = "about"
    }
}
