package jp.yuta.kohashi.sotsuseicameraapp.utils.permission

/**
 * Author : yutakohashi
 * Project name : FakeLineApp
 * Date : 30 / 08 / 2017
 */

internal interface OnActivityPermissionCallback{
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
}