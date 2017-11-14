package com.linheimx.zimudog.vp.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by ari on 8/16/16.
 */
abstract class BaseFragment : Fragment() {

    lateinit var _CompositeDisposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(_ProvideLayout(), null)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _CompositeDisposable = CompositeDisposable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _CompositeDisposable.dispose()
    }

    fun addDispose(dispose: Disposable) {
        _CompositeDisposable.add(dispose)
    }


    /**
     * 提供根布局
     *
     * @return
     */
    abstract fun _ProvideLayout(): Int
}

