package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.widget.Toast
import jp.yuta.kohashi.sotsuseicameraapp.App
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.utils.ResUtil


/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 11 / 10 / 2017
 */

object ToastHelper {

    fun alreadyRunningService() = show(ResUtil.string(R.string.already_running_servce))

    fun runService() = show(ResUtil.string(R.string.success_runnning_service))

    fun stopService() = show(ResUtil.string(R.string.success_stop_servivce))

    fun alreadyStopService() = show(ResUtil.string(R.string.already_stop_service))

    fun show(text: String) {
        Toast.makeText(App.context, text, Toast.LENGTH_SHORT).show()
    }

    fun showLong(text: String) {
        Toast.makeText(App.context, text, Toast.LENGTH_LONG).show()
    }

}