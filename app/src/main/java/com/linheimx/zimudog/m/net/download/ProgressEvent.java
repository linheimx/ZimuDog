package com.linheimx.zimudog.m.net.download;

/**
 * Created by x1c on 2017/5/6.
 */

public class ProgressEvent {
    private String urlKey;

    private long progress;
    private long max;

    private boolean isError;
    private String errorMsg;

    public ProgressEvent(String urlKey, long progress, long max) {
        this.urlKey = urlKey;
        this.progress = progress;
        this.max = max;
    }

    public ProgressEvent(String urlKey, boolean isError, String errorMsg) {
        this.urlKey = urlKey;
        this.isError = isError;
        this.errorMsg = errorMsg;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
