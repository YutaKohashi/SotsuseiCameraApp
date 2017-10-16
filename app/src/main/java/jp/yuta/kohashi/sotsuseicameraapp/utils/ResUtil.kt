package jp.yuta.kohashi.sotsuseicameraapp.utils

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import jp.yuta.kohashi.sotsuseicameraapp.App

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 11 / 10 / 2017
 */

object ResUtil{
    /**
     * Drawableリソース取得
     * @param id
     * @return
     */
    fun string(@StringRes id: Int): String =  App.context.resources.getString(id)

    /**
     * Drawableリソース取得
     * @param id
     * @return
     */
    fun drawable(@DrawableRes id: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
             App.context.resources.getDrawable(id, null)
        else
             App.context.resources.getDrawable(id)
    }

    /**
     * colorリソース取得
     * @param id
     * @return
     */
    fun color(@ColorRes id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
             App.context.resources.getColor(id, null)
        else
             App.context.resources.getColor(id)
    }
}