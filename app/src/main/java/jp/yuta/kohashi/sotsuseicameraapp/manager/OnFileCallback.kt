package jp.yuta.kohashi.filemanagersampleapp.managers

import android.graphics.Bitmap

/**
 * Author : yutakohashi
 * Project name : FileManagerSampleApp
 * Date : 31 / 08 / 2017
 */

interface OnFileCallback{

    /**
     * callback when text file exist
     */
    fun OnExistedFile(filePath:String, fileName:String, data:String)

    /**
     * callback when text bitmap exist
     */
    fun OnExistedFile(filePath:String, fileName:String, data:Bitmap)


    fun OnPermissionDenied()
}