package com.hu.p7zip

/**
 * Created by LJIAN on 2017/5/11.
 */
object ZipUtils {

    /**
     * Execute a command
     * @param command command string
     * @return return code
     */
    external fun executeCommand(command: String): Int

    /**
     * load native library
     */
    init {
        System.loadLibrary("p7zip")
    }
}
