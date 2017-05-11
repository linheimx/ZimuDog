package com.hu.p7zip;

/**
 * Created by LJIAN on 2017/5/11.
 */
public class ZipUtils {

    /**
     * Execute a command
     * @param command command string
     * @return return code
     */
    public static native int executeCommand(String command);

    /**
     * load native library
     */
    static {
        System.loadLibrary("p7zip");
    }
}
