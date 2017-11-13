package com.linheimx.zimudog.vp.browzimu

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.linheimx.zimudog.R
import com.linheimx.zimudog.vp.base.BaseFragment
import java.util.ArrayList
import com.linheimx.zimudog.utils.bindView

class BrowZimuFragment : BaseFragment() {

    val tabLayout: TabLayout by bindView(R.id.tab)
    val viewPager: ViewPager by bindView(R.id.vp)

    override fun _ProvideLayout(): Int {
        return R.layout.fragment_brow
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // viewpager
        val downloadingFragment = DownloadingFragment()
        val sdCardFragment = SdCardFragment()

        val list = ArrayList<TitleFragment>()
        list.add(sdCardFragment)
        list.add(downloadingFragment)

        val vpAdapter = VPAdapter(childFragmentManager, list)
        viewPager!!.adapter = vpAdapter
        viewPager!!.offscreenPageLimit = 2

        // tab
        tabLayout!!.setupWithViewPager(viewPager)
    }
}

class VPAdapter(fm: FragmentManager, val _list: List<TitleFragment>) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return _list.size
    }

    override fun getItem(position: Int): Fragment {
        return _list[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return _list[position].title
    }
}
