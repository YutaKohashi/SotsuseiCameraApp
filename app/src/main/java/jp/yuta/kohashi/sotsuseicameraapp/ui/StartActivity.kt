package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.app.Activity

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 09 / 10 / 2017
 */
interface StartActivity{
    /**
     *  ex.
     *   companion object : StartActivity {
     *       override fun start(activity: Activity)  = activity.startActivity(Intent(activity,RunningActivity::class.java))
     *   }
     */
    fun start(activity: Activity)

}