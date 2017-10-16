package jp.yuta.kohashi.sotsuseicameraapp.ui.home

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

class HomeActivity : BaseActivity() {


    companion object : StartActivity {
        override fun start(activity: Activity) = activity.startActivity(Intent(activity, HomeActivity::class.java))
    }

    override val fragment: Fragment?
        get() = HomeFragment()


    override fun setEvent() {


    }
}