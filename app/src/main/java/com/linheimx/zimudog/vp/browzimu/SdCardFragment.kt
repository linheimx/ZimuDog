package com.linheimx.zimudog.vp.browzimu

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.hu.p7zip.ZipUtils
import com.linheimx.zimudog.App
import com.linheimx.zimudog.R
import com.linheimx.zimudog.m.bean.Ok
import com.linheimx.zimudog.utils.Utils
import java.io.File
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Arrays
import java.util.Date
import es.dmoral.toasty.Toasty
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import com.linheimx.zimudog.utils.Utils.convertFileSize
import com.linheimx.zimudog.utils.bindView
import com.linheimx.zimudog.utils.rxbus.RxBus_Behavior
import io.reactivex.functions.Consumer

/**
 * Created by LJIAN on 2017/5/9.
 */

class SdCardFragment : TitleFragment() {

    val _rv: RecyclerView by bindView(R.id.rv)
    val _tv_nav: TextView by bindView(R.id.tv_nav)
    val _srl: SwipeRefreshLayout by bindView(R.id.srl)

    lateinit var _QuickAdapter: QuickAdapter

    override val title: String
        get() = "已下载"

    val progressDialog: ProgressDialog by lazy { ProgressDialog(activity) }

    override fun _ProvideLayout(): Int = R.layout.fragment_sdcard

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _tv_nav.setOnClickListener { onNav() }

        _rv!!.setHasFixedSize(true)
        _rv!!.layoutManager = LinearLayoutManager(activity)

        _QuickAdapter = QuickAdapter()
        _QuickAdapter.bindToRecyclerView(_rv)
        _QuickAdapter.openLoadAnimation()
        _QuickAdapter.setEmptyView(R.layout.rv_empty_view_sdcard)

        RxBus_Behavior.toFlowable(Ok::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _rv.postDelayed({ _QuickAdapter.filesChanged() }, 200)
                })

        _srl!!.setOnRefreshListener {
            _rv!!.postDelayed({
                _QuickAdapter.rest2ZimuDog()
                Toasty.info(App.get()!!, "定位到了 手机的内部存储/ZimuDog目录", Toast.LENGTH_LONG).show()
                _srl!!.isRefreshing = false
            }, 200)
        }
    }

    override fun onResume() {
        super.onResume()

        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                // handle back button's click listener
                onNav()
                return@OnKeyListener true
            }
            false
        })

        _QuickAdapter.filesChanged()
    }

    fun onNav() {
        val currentDir = _QuickAdapter.currentDir
        val parent = currentDir.parentFile
        if (parent != null) {
            // 安全检查
            if (parent.listFiles() == null) {
                return
            }

            _QuickAdapter.currentDir = parent
            _QuickAdapter.filesChanged()
        }
    }


    inner class QuickAdapter : BaseQuickAdapter<File, BaseViewHolder>(R.layout.rv_item_sdcard) {

        lateinit var currentDir: File

        init {
            rest2ZimuDog()
        }

        fun rest2ZimuDog() {
            val dir_zimu = File(Utils.rootDirPath)
            currentDir = dir_zimu
            filesChanged()
        }

        fun filesChanged() {
            _tv_nav!!.text = currentDir.path
            val files = currentDir.listFiles()

            if (files != null && files.size > 0) { // check
                Arrays.sort(files) { o1, o2 ->
                    val ret = o1.lastModified() - o2.lastModified()
                    if (ret > 0) {
                        -1
                    } else if (ret == 0L) {
                        0
                    } else {
                        1
                    }
                }
                val fileList = Arrays.asList(*files)

                if (mData != null) {
                    mData.clear()
                }
                // make sure length > 0
                try {
                    for (file in fileList) {
                        if (file.length() > 0) {
                            mData.add(file)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                mData = ArrayList()
            }

            notifyDataSetChanged()
        }

        override fun convert(helper: BaseViewHolder, file: File) {

            // type
            val tv = helper.getView<TextView>(R.id.tv_type)
            if (file.isFile) {
                tv.setBackgroundColor(ContextCompat.getColor(activity, R.color.tv_file))
                val name = file.name
                try {
                    val types = name.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    if (types != null && types.size > 0) {
                        helper.setText(R.id.tv_type, types[types.size - 1])
                    }
                } catch (e: Exception) {

                }

            } else {
                tv.setBackgroundColor(ContextCompat.getColor(activity, R.color.tv_dir))
                tv.text = ""
            }

            // title
            helper.setText(R.id.tv_name, file.name)

            // size
            var size = ""
            try {
                if (file.isFile) {
                    size = convertFileSize(file.length())
                } else {
                    size = file.listFiles().size.toString() + "项"
                }
            } catch (e: Exception) {

            }

            helper.setText(R.id.tv_size, size)

            // time
            val currentTime = Date(file.lastModified())
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateString = formatter.format(currentTime)
            helper.setText(R.id.tv_time, dateString + "")

            helper.itemView.setOnClickListener {
                if (file.isFile) {
                    showMenu(file)
                } else {
                    currentDir = file
                    filesChanged()
                }
            }

            helper.itemView.setOnLongClickListener {
                if (!file.isFile) {
                    showMenu(file)
                }
                true
            }
        }
    }

    private fun showMenu(file: File) {
        val dialog = BottomSheetBuilder(activity)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem("选择操作")
                .addItem(0, "删除", R.drawable.op_delete)
                .addItem(1, "解压", R.drawable.op_open)
                .setItemClickListener { item ->
                    if (item.title == "删除") {
                        op_delete(file)
                    } else if (item.title == "解压") {
                        // check
                        if (file.name.endsWith("zip") || file.name.endsWith("rar")) {
                            op_unZipRar(file)
                        } else {
                            Toasty.info(App.get()!!, "不支持的解压格式", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.createDialog()
        dialog.show()
    }

    private fun op_delete(file: File) {
        deleFileRe(file)
        _QuickAdapter.filesChanged()
    }

    /**
     * 递归删除文件
     *
     * @param file
     */
    private fun deleFileRe(file: File) {
        if (file.isFile) {
            file.delete()
        } else {
            for (f in file.listFiles()) {
                deleFileRe(f)
            }
            if (file.listFiles().size == 0) {
                file.delete()
            }
        }
    }


    private fun op_unZipRar(file: File) {
        progressDialog.setTitle("解压中...")
        progressDialog.show()

        Flowable
                .fromCallable {
                    var ret = -1
                    try {
                        Thread.sleep(1000)//延时一下

                        val parentPath = file.parent

                        // 考虑到文件名字的原因,重命名！
                        val originFileName = file.name
                        val newFileName = parentPath + "/" + System.currentTimeMillis()
                        val tmpFile = File(newFileName)
                        file.renameTo(tmpFile)

                        // out dir
                        val tmpDir = File(file.parent + "/d" + System.currentTimeMillis())
                        if (!tmpDir.exists()) {
                            tmpDir.mkdir()
                        } else {
                            deleFileRe(tmpDir)
                            tmpDir.mkdir()
                        }

                        // cmd
                        val cmd = StringBuilder("7z x")
                        cmd.append(" " + tmpFile.path)    //7z x 'aaa/bbb.zip'
                        cmd.append(" -o" + tmpDir.path + "")    //7z x 'a.zip' '-o/out/'
                        ret = ZipUtils.executeCommand(cmd.toString())

                        // 恢复名称
                        val finalFile = File(parentPath + "/" + originFileName)
                        finalFile.createNewFile()
                        tmpFile.renameTo(finalFile)

                        val dirName = originFileName.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        val finalDir = File(parentPath + "/" + dirName)
                        if (finalDir.exists()) {
                            deleFileRe(finalDir)
                        }
                        finalDir.mkdir()
                        tmpDir.renameTo(finalDir)

                    } catch (e: InterruptedException) {
                        Log.e("===>",e.message)
                        e.printStackTrace()
                    }

                    ret != -1
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _QuickAdapter.filesChanged()
                    progressDialog!!.dismiss()
                    if (!it) {
                        Toasty.error(App.get()!!, "解压失败", Toast.LENGTH_LONG).show()
                    }
                })
    }

}
