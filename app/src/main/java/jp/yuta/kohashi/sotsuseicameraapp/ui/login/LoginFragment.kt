package jp.yuta.kohashi.sotsuseicameraapp.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.SotsuseiApiHelper
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.exception.ApiException
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.model.Model
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.debug.DebugActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.debug.DebugClass
import jp.yuta.kohashi.sotsuseicameraapp.ui.home.HomeActivity
import jp.yuta.kohashi.sotsuseicameraapp.utils.NetworkUtil
import jp.yuta.kohashi.sotsuseicameraapp.utils.PrefUtil
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 27 / 09 / 2017
 */
class LoginFragment : BaseFragment() {

    override val sLayoutRes: Int
        get() = R.layout.fragment_login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setEvent() {
        storeIdEditText.setText("00000")
        storePassEditText.setText("00000")

        //
        val token = PrefUtil.storeToken
        if (!TextUtils.isEmpty(token)) {
            val storeId = PrefUtil.storeId
            SotsuseiApiHelper.postStoreVerifyToken(storeId, token,
                    object : SotsuseiApiHelper.Callback<Model.DefaultResponse> {
                        override fun onSuccess(body: Model.DefaultResponse?) {
                            activityStart<HomeActivity>(Bundle().apply { putBoolean("successLogin", true) })
                            activity.finishAffinity()
                        }

                        override fun onFailure(e: ApiException) {
                            (activity as LoginActivity).showSnackBar("tokenが無効です")
//                            PrefUtil.storeToken = ""
//                            PrefUtil.empToken = ""
                        }
                    })
        }

        loginButton.setOnClickListener {
            if(!NetworkUtil.isConnectNetwork()) {
                (activity as LoginActivity).showSnackBar("ネットワークに接続されていません")
                return@setOnClickListener
            }

            val prog = ProgressDialog(context!!)
            prog.setMessage("ログイン中...")
            prog.setCancelable(false)
            prog.show()
            val storeId = storeIdEditText.text.toString()
            val storePassword = storePassEditText.text.toString()

            SotsuseiApiHelper.postStoreCreateToken(storeId, storePassword,
                    object : SotsuseiApiHelper.Callback<Model.TokenResponse> {
                        override fun onSuccess(body: Model.TokenResponse?) {
                            if (body != null) {
                                PrefUtil.storeToken = body.token
                                PrefUtil.storeId = storeId
                                PrefUtil.storePass = storePassword
                                activity.finish()
                                activityStart<HomeActivity>(Bundle().apply { putBoolean("successLogin", true) })
                            } else {
                                //                            Toast.makeText(App.context,"error",Toast.LENGTH_SHORT).show()
                                (activity as LoginActivity).showSnackBar("error")
                                // TODO
//                                activity.finish()
//                                activityStart<HomeActivity>()
                            }

                            prog.dismiss()
                        }

                        override fun onFailure(e: ApiException) {
                            //                            Toast.makeText(App.context,"error",Toast.LENGTH_SHORT).show()
                            (activity as LoginActivity).showSnackBar("error")
                            // TODO
//                            activity.finish()
//                            activityStart<HomeActivity>()
                            prog.dismiss()
                        }
                    })
        }

        loginButton.setOnTouchListener(DebugClass(context,{
            activityStart<DebugActivity>()
        }))
    }

    override fun onDestroy() {
        super.onDestroy()
        SotsuseiApiHelper.dispose()
    }

}