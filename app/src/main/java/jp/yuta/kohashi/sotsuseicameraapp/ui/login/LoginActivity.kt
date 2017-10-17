package jp.yuta.kohashi.sotsuseicameraapp.ui.login

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.StartActivity

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

class LoginActivity : BaseActivity() {

    companion object : StartActivity {

        override fun start(activity: Activity) = activity.startActivity(Intent(activity, LoginActivity::class.java))
    }

    override val fragment: Fragment?
        get() = LoginFragment()

    override fun setEvent() {

    }

}
