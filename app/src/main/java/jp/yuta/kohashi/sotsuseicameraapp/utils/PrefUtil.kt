package jp.yuta.kohashi.sotsuseicameraapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

object PrefUtil {
    private lateinit var prefs: SharedPreferences

    fun setUp(context: Context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    var userId: String
        get() = prefs.getString(KEY_USER_ID,"")
        set(value) { prefs.edit().putString(KEY_USER_ID, value).apply() }

    var userPass:String
        get() = prefs.getString(KEY_USER_PASS, "")
        set(value) {prefs.edit().putString(KEY_USER_PASS, value).apply()}


    private val KEY_USER_ID = "user_id"
    private val KEY_USER_PASS = "user_pass"
    private val KEY_TENPO_ID = "tenpo_id"
    private val KEY_TENPO_PASS = "tenpo_pass"
    private val KEY_IS_RUNNING = "is_running"
}