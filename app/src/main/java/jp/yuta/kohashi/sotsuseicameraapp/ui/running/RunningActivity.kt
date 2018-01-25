package jp.yuta.kohashi.sotsuseicameraapp.ui.running

import android.app.Activity
import android.support.v4.app.Fragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.StartActivity


/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */
class RunningActivity : BaseActivity() {

    companion object : StartActivity<RunningActivity> {
        override fun start(activity: Activity) = super.start(activity, RunningActivity::class.java)
    }

    override val KEEP_SCREEN_ON: Boolean = true
    override val FULL_SCREEN: Boolean = true

    override val fragment: Fragment? = RunningFragment()

    override fun setEvent() {

    }
}