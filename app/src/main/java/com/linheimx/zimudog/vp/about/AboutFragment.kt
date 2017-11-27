package com.linheimx.zimudog.vp.about


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView

import com.linheimx.zimudog.R
import com.linheimx.zimudog.vp.base.BaseFragment
import com.linheimx.zimudog.utils.bindView

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : BaseFragment() {

    val tv_howto: TextView by bindView(R.id.btn_howto)

    override fun _ProvideLayout(): Int {
        return R.layout.fragment_about
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_howto.setOnClickListener {
            val dialog = HowToDialog()
            dialog.show(childFragmentManager, null)
        }
    }
}
