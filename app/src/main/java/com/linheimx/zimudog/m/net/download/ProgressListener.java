package com.linheimx.zimudog.m.net.download;

/**
 * Created by x1c on 2017/5/4.
 */

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}