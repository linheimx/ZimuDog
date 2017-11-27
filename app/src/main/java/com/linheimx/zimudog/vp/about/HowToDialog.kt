package com.linheimx.zimudog.vp.about

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.linheimx.zimudog.R
import com.linheimx.zimudog.utils.Utils
import com.linheimx.zimudog.utils.bindView
import com.zzhoujay.richtext.RichText

/**
 * Created by x1c on 2017/4/23.
 */

class HowToDialog : DialogFragment() {

    val _tv: TextView by bindView(R.id.tv)
    lateinit var _view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _view = inflater!!.inflate(R.layout.dialog_howto, null)
        _view.y = -2500f
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val c = Utils.getStringFromAssetFile(activity!!.assets, "howto.md")
        RichText.fromMarkdown(c).into(_tv)
    }

    override fun onResume() {
        super.onResume()

        // show the view
        _view.animate()
                .alpha(1f)
                .y(0f)
                .setDuration(680)
                .start()

        // set dialog
        if (dialog != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        }
    }
}
