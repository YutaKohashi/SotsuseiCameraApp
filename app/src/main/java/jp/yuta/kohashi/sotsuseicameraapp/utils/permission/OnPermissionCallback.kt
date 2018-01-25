package jp.yuta.kohashi.sotsuseicameraapp.utils.permission


/**
 * Author : yutakohashi
 * Project name : FakeLineApp
 * Date : 30 / 08 / 2017
 */
interface OnPermissionCallback{

    fun onGrantedPermission(permissionName: Array<String>)

    fun onDeniedPermission(permissionName: Array<String>)

    fun onPreGrantedPermission(permissionsName: String)

    fun onNeedExplanationPermission(permissionName: String)

    fun onReallyDeniedPermission(permissionName: String)

    fun onNoNeededPermission()
}