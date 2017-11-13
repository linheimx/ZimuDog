package com.linheimx.zimudog.utils.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.linheimx.zimudog.R


/**
 * Created by x1c on 2017/4/23.
 */

class LoadingDialog : DialogFragment() {

    internal var onCancelListener: DialogInterface.OnCancelListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_loading, null)
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog
        if (dialog != null) {
            if (onCancelListener != null) {
                dialog.setOnCancelListener(onCancelListener)
            }
        }
    }

    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?) {
        this.onCancelListener = onCancelListener

        val dialog = dialog
        if (dialog != null) {
            if (onCancelListener != null) {
                dialog.setOnCancelListener(onCancelListener)
            }
        }
    }
}
