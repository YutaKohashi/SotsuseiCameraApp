package jp.yuta.kohashi.sotsuseicameraapp.utils.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log


/**
 * Author : yutakohashi
 * Project name : FakeLineApp
 * Date : 29 / 08 / 2017
 */
@RequiresApi(Build.VERSION_CODES.M)
class PermissionHelper : OnActivityPermissionCallback, OnActivityForResult {
    private val TAG = PermissionHelper::class.java.simpleName

    private val REQUEST_OVERLAY_PERMISSION: Int = 1000
    private val REQUEST_PERMISSIONS: Int = 1001
    private val REQUEST_MY_APP_SETTINGS: Int = 1002

    private var mContext: Activity?
    private var mFragment: Fragment?
    private var mForceAccepting: Boolean = false // default false
    private var mOnPermissionCallback: OnPermissionCallback
    private val OVER_MARSHMALLOW: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    private val IS_FROM_ACTIVITY: Boolean // use from activity?

    companion object {
        fun instance(context: Activity, callback: OnPermissionCallback? = null): PermissionHelper = PermissionHelper(context, callback)
        fun instance(fragment: Fragment, callback: OnPermissionCallback? = null): PermissionHelper = PermissionHelper(fragment, callback)
    }

    /**
     *when use this class from activity
     */
    private constructor(context: Activity, callback: OnPermissionCallback? = null) {
        mContext = context
        mFragment = null

        mOnPermissionCallback = callback ?:
                if (mContext is OnPermissionCallback) mContext as OnPermissionCallback
                else throw IllegalArgumentException("OnPermissionCallbackが実装されていない")

        IS_FROM_ACTIVITY = true
    }

    /**
     * when use this class from fragment
     */
    private constructor(fragment: Fragment, callback: OnPermissionCallback? = null) {
        mContext = null
        mFragment = fragment

        mOnPermissionCallback = callback ?:
                if (mFragment is OnPermissionCallback) mFragment as OnPermissionCallback
                else throw IllegalArgumentException("OnPermissionCallbackが実装されていない")

        IS_FROM_ACTIVITY = false
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (verifyIsGrantedPermissions(grantResults)) {
                mOnPermissionCallback.onGrantedPermission(permissions)
            } else {
                val deniedPermissions = getDeniedPermissions(permissions)
                var deniedPermissionFlg = false
                deniedPermissions.forEach { deniedPermission ->
                    if (!isNeededExplanation(deniedPermission)) {
                        mOnPermissionCallback.onReallyDeniedPermission(deniedPermission)
                        deniedPermissionFlg = true
                    }
                }

                if (!deniedPermissionFlg) {
                    if (mForceAccepting) requestAfterExplanation(permissions)
                    else mOnPermissionCallback.onDeniedPermission(permissions)
                }
            }
        }
    }

    /**
     * startActivityForResultで起動したintentの戻り値を取得する
     */
    override fun onActivityResult(requestCode: Int) {
        if (OVER_MARSHMALLOW) {
            when (requestCode) {
                REQUEST_OVERLAY_PERMISSION -> {
                    if (isGrantedSystemAlertPermission())
                        mOnPermissionCallback.onGrantedPermission(arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW))
                    else
                        mOnPermissionCallback.onDeniedPermission(arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW))
                }
            /**
             *
             */
                REQUEST_MY_APP_SETTINGS -> {

                }
            }
        } else {
            mOnPermissionCallback.onPreGrantedPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
        }
    }

    fun setForceAccepting(forceAccepting: Boolean): PermissionHelper {
        mForceAccepting = forceAccepting
        return this
    }

    /**
     * permission request
     */
    fun request(permission: String): PermissionHelper {
        if (OVER_MARSHMALLOW) {
            handleRequest(permission)
        } else {
            mOnPermissionCallback.onNoNeededPermission()
        }
        return this
    }

    fun request(permission:ArrayList<String>):PermissionHelper{
        return request(permission.toTypedArray())
    }

    fun request(permissions: Array<String>): PermissionHelper {
        if (OVER_MARSHMALLOW) {
            val denies = getDeniedPermissions(permissions)
            if (denies.count() == 0) mOnPermissionCallback.onGrantedPermission(permissions)
            denies.forEach { permissionName -> handleRequest(permissionName) }
        } else {
            mOnPermissionCallback.onNoNeededPermission()
        }
        return this
    }


    private fun handleRequest(permission: String) {
        if (isDefinePermissionInManifest(permission)) {
            if (!permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW, true)) {
                if (isDeniedPermission(permission)) {
                    if (isNeededExplanation(permission)) {
                        mOnPermissionCallback.onNeedExplanationPermission(permission)
                    } else {
                        if (IS_FROM_ACTIVITY) ActivityCompat.requestPermissions(mContext!!, arrayOf(permission), REQUEST_PERMISSIONS)
                        else mFragment!!.requestPermissions(arrayOf(permission), REQUEST_PERMISSIONS)
                    }
                } else {
                    mOnPermissionCallback.onPreGrantedPermission(permission)
                }
            } else {
                requestSystemAlertPermission()
            }
        } else {
            mOnPermissionCallback.onDeniedPermission(arrayOf(permission))
        }
    }

    fun requestAfterExplanation(permissions:ArrayList<String>){
        requestAfterExplanation(permissions.toTypedArray())
    }

    fun requestAfterExplanation(permissions: Array<String>) {
        val permissionsToRequest: MutableList<String> = mutableListOf()
        permissions.forEach { p ->
            if (isDeniedPermission(p)) permissionsToRequest.add(p)
            else mOnPermissionCallback.onPreGrantedPermission(p)
        }

        if (permissionsToRequest.isEmpty()) return
        if (IS_FROM_ACTIVITY) ActivityCompat.requestPermissions(mContext!!, permissionsToRequest.toTypedArray(), REQUEST_PERMISSIONS)
        else mFragment!!.requestPermissions(permissionsToRequest.toTypedArray(), REQUEST_PERMISSIONS)
    }

    fun requestAfterExplanation(permission: String) {
        requestAfterExplanation(arrayOf(permission))
    }

    /**
     * 拒否されているpermissionのみ取得
     */
    fun getDeniedPermissions(permissions: Array<String>?): Array<String> {
        val declines: MutableList<String> = mutableListOf()
        permissions?.forEach { permissionName ->
            if (ActivityCompat.checkSelfPermission(if (IS_FROM_ACTIVITY) mContext!! else mFragment!!.context!!, permissionName) != PackageManager.PERMISSION_GRANTED)
                declines.add(permissionName)
        }
        return declines.toTypedArray()
    }


    /**
     * manifestファイルに定義されているか
     */
    fun isDefinePermissionInManifest(permission: String): Boolean {
        getDefinePermissions()?.forEach { permissionName ->
            if (permissionName.equals(permission)) return true
        }
        return false
    }

    fun getDefinePermissions(): Array<String>? {
        return try {
            (if (IS_FROM_ACTIVITY) mContext!! else mFragment!!.context)?.
                    packageManager?.getPackageInfo((if (IS_FROM_ACTIVITY) mContext!! else mFragment!!.context)?.
                    packageName, PackageManager.GET_PERMISSIONS)?.
                    requestedPermissions
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun openMyApplicationSettings() {
        try {
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + (if (IS_FROM_ACTIVITY) mContext!! else mFragment!!.context)?.packageName)
            }

            if (IS_FROM_ACTIVITY) mContext!!.startActivityForResult(intent, REQUEST_MY_APP_SETTINGS)
            else mFragment?.startActivityForResult(intent, REQUEST_MY_APP_SETTINGS)

        } catch (e: NullPointerException) {

        }
    }

    fun verifyIsGrantedPermissions(grantResults: IntArray): Boolean {
        if (grantResults.isEmpty()) return false
        grantResults.forEach { if (it != PackageManager.PERMISSION_GRANTED) return false }
        return true
    }

    //**
    //region SystemAlertPermission
    //**

    /**
     * SystemAlertPermissionの場合のみ通常のダイアログではできない
     */
    fun requestSystemAlertPermission() {
        if (OVER_MARSHMALLOW) {
            try {
                if (!isGrantedSystemAlertPermission())
                // systemalertpermimssion　が許可されていない
                    if (IS_FROM_ACTIVITY) mContext!!.startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mContext!!.packageName)), REQUEST_OVERLAY_PERMISSION)
                    else mFragment!!.startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mFragment!!.context?.packageName)), REQUEST_OVERLAY_PERMISSION)
                else
                    mOnPermissionCallback.onPreGrantedPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
            } catch (e: Exception) {
                Log.e(TAG, "requestSystemAlertPermission : " + e.toString())
            }
        } else {
            // marshmallow未満はデフォルトで許可
            mOnPermissionCallback.onPreGrantedPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isGrantedSystemAlertPermission(): Boolean {
        return if (OVER_MARSHMALLOW) Settings.canDrawOverlays(if (IS_FROM_ACTIVITY) mContext!! else mFragment!!.context)
        else true
    }

    //**
    //endregion
    //**

    /**
     * permissionが拒否されているか
     */
    fun isDeniedPermission(permissionsName: String): Boolean =
            ActivityCompat.checkSelfPermission(if (IS_FROM_ACTIVITY) mContext!! else mFragment!!.context!!, permissionsName) != PackageManager.PERMISSION_GRANTED


    /**
     * permissionが許可されているか
     */
    fun isGrantedPermission(permissionsName: String): Boolean =
            ActivityCompat.checkSelfPermission(if (IS_FROM_ACTIVITY) mContext!! else mFragment!!.context!!, permissionsName) == PackageManager.PERMISSION_GRANTED


    /**
     * 許可ダイアログの再表示判定(永続的に不許可設定の場合、falseが返却される）
     */
    fun isNeededExplanation(permissionName: String): Boolean =
            if (IS_FROM_ACTIVITY) ActivityCompat.shouldShowRequestPermissionRationale(mContext!!, permissionName)
            else mFragment!!.shouldShowRequestPermissionRationale(permissionName)

    /**
     * 設定画面でしか権限を設定できないときtrue
     *
     *
     * consider using [PermissionHelper.openMyApplicationSettings] to open settings screen
     */
    fun isPermissionPermanentlyDenied(permission: String): Boolean =
            isDeniedPermission(permission) && !isNeededExplanation(permission)



}