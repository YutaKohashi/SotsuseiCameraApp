package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.app.Activity
import android.content.Intent

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 09 / 10 / 2017
 */
interface StartActivity<T : Activity> {
    /**
     *  ex.
     *   companion object :StartActivity<LoginActivity> {
     *      override fun start(activity: Activity) = super.start(activity,LoginActivity::class.java)
     *  }
     */
    fun start(activity: Activity, clazz: Class<T>) = activity.startActivity(Intent(activity, clazz))

    fun start(activity: Activity)

}