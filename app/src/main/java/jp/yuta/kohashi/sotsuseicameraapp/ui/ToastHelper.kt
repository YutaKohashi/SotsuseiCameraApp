package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.support.annotation.StringRes
import android.widget.Toast
import jp.yuta.kohashi.sotsuseicameraapp.App
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.utils.ResUtil


/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 11 / 10 / 2017
 */

object  ToastHelper {

    fun alreadyRunningService() = show(R.string.already_running_servce)

    fun runService() = show(R.string.success_runnning_service)

    fun stopService() = show(R.string.success_stop_servivce)

    fun alreadyStopService() = show(R.string.already_stop_service)

    fun cameraAccessException() = show(R.string.camera_access_exception)

    fun noPermission() = show(R.string.no_permission)

    fun show(@StringRes id:Int) = show(ResUtil.string(id))

    fun show(text: String) {
        Toast.makeText(App.context, text, Toast.LENGTH_SHORT).show()
    }

    fun showLong(text: String) {
        Toast.makeText(App.context, text, Toast.LENGTH_LONG).show()
    }

}