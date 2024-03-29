package jp.yuta.kohashi.sotsuseicameraapp.ui.login

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.StartActivity

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

class LoginActivity: BaseActivity(){

    companion object : StartActivity<LoginActivity> {
        override fun start(activity: Activity) = super.start(activity,LoginActivity::class.java)
    }


    override val fragment: Fragment? = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.getBooleanExtra("successLogout",false)){
            showSnackBar("ログアウトしました",500)
            intent.putExtra("successLogout",false)
        }
    }
    override fun setEvent() {

    }

}
