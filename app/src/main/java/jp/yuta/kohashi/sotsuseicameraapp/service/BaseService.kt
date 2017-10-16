package jp.yuta.kohashi.sotsuseicameraapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 12 / 10 / 2017
 */
abstract class BaseService: Service(){


    /**
     * 使用しない
     */
    override fun onBind(p0: Intent?): IBinder? = null
}
