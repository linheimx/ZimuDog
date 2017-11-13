package com.linheimx.zimudog.m.net.download

/**
 * Created by x1c on 2017/5/4.
 */

interface ProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}