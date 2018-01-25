package jp.yuta.kohashi.sotsuseicameraapp.ui.home


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.SotsuseiApiHelper
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.exception.ApiException
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.model.Model
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseDrawerActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.StartActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.debug.DebugActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.info.InfoActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.login.LoginActivity
import jp.yuta.kohashi.sotsuseicameraapp.utils.NetworkUtil
import jp.yuta.kohashi.sotsuseicameraapp.utils.PrefUtil
import jp.yuta.kohashi.sotsuseicameraapp.utils.ResUtil
import jp.yuta.kohashi.sotsuseicameraapp.utils.permission.OnPermissionCallback
import jp.yuta.kohashi.sotsuseicameraapp.utils.permission.PermissionHelper

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

class HomeActivity : BaseDrawerActivity(), OnPermissionCallback {

    companion object : StartActivity<HomeActivity> {
        override fun start(activity: Activity) = super.start(activity, HomeActivity::class.java)
    }

    override val containerFragment: Fragment? = HomeFragment()

    override val menuItemFromRes: Int? = R.menu.menu_drawer

    override val headerViewFromRes: Int? = R.layout.header_drawer

    private lateinit var mNavigationButton: View

    private lateinit var mPermissionHelper: PermissionHelper

    private val REQUEST_PERMISSIONS: ArrayList<String> = arrayListOf<String>().apply {
        add(Manifest.permission.READ_EXTERNAL_STORAGE)
        add(Manifest.permission.CAMERA)
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState, false)
        mNavigationButton = defaultDrawerToggle()

        mContainerView.addView(mNavigationButton)
        mContainerView.bringChildToFront(mNavigationButton)
        mContainerView.requestLayout()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionHelper = PermissionHelper.instance(this).setForceAccepting(true)

            mPermissionHelper.request(REQUEST_PERMISSIONS)
        }

        if(intent.getBooleanExtra("successLogin",false)) {
            showSnackBar("ログインしました",700)
            intent.putExtra("successLogin",false)
        }
        setEvent()
    }

    override fun setEvent() {
        mNavigationView.setBackgroundColor(ResUtil.color(R.color.bg_main))

//        Util.setImageByGlide(R.drawable.lawson, mNavigationView.getHeaderView(0).findViewById<ImageView>(R.id.headerImageView))

        mNavigationButton.setOnClickListener {
            openDrawer()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_01 -> activityStart<InfoActivity>()
            R.id.item_03  -> logout()
            R.id.item_debug -> DebugActivity.start(this)
        }
        return super.onNavigationItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) mPermissionHelper.onActivityResult(requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onGrantedPermission(permissionName: Array<String>) {

    }

    override fun onDeniedPermission(permissionName: Array<String>) {

    }

    override fun onPreGrantedPermission(permissionsName: String) {

    }

    override fun onNeedExplanationPermission(permissionName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mPermissionHelper.requestAfterExplanation(REQUEST_PERMISSIONS)
    }

    override fun onReallyDeniedPermission(permissionName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    mPermissionHelper.openMyApplicationSettings()
                } catch (e: Exception) {

                }
            }, 2300)

    }

    override fun onNoNeededPermission() {
    }

    private fun logout(){
        if(!NetworkUtil.isConnectNetwork()) {
            PrefUtil.storeToken = ""
            activityStart<LoginActivity>(Bundle().apply { putBoolean("successLogout",true) })
            finishAffinity()
            return
        }

        val prog = ProgressDialog(this)
        prog.setMessage("ログイン中...")
        prog.setCancelable(false)
        prog.show()

        val token = PrefUtil.storeToken
        SotsuseiApiHelper.postRevocationToken(token,
                object : SotsuseiApiHelper.Callback<Model.DefaultResponse> {
                    override fun onSuccess(body: Model.DefaultResponse?) {
                        if (body != null) {

                        } else {
                            showSnackBar("error")
                        }
                        prog.dismiss()
                        PrefUtil.storeToken = ""
                        activityStart<LoginActivity>(Bundle().apply { putBoolean("successLogout",true) })
                        finishAffinity()
                    }

                    override fun onFailure(e: ApiException) {
                        showSnackBar("error")
                        prog.dismiss()
                        PrefUtil.storeToken = ""
                        activityStart<LoginActivity>(Bundle().apply { putBoolean("successLogout",true) })
                        finishAffinity()
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        SotsuseiApiHelper.dispose()
    }
}

