package jp.yuta.kohashi.sotsuseicameraapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import jp.yuta.kohashi.sotsuseicameraapp.utils.PrefUtil

/**
 * Author : yutakohashi
 * Project name : TwitterLiteProject
 * Date : 25 / 07 / 2017
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        PrefUtil.setUp(context)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            get
            private set
    }
}
