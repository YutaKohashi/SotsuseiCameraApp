package jp.yuta.kohashi.sotsuseicameraapp.utils


import android.content.res.Configuration
import jp.yuta.kohashi.sotsuseicameraapp.App
import java.util.*


/**
 * Author : yutakohashi
 * Project name : FakeLineApp
 * Date : 27 / 06 / 2017
 */
object DisplayUtil {
    /**
     * 端末のDisplayの横幅取得
     * @return
     */
    val displayWidthPixels: Int
        get() = App.context.resources.displayMetrics.widthPixels

    /**
     * 端末のDisplayの縦幅取得
     * @return
     */
    val displayHeightPixels: Int
        get() = App.context.resources.displayMetrics.heightPixels

    /**
     * dpをpxに変換
     * @param dp
     * @return
     */
    fun dp2px(dp: Float): Int {
        val density = App.context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    /**
     * pxをdpに変換
     * @param px
     * @return
     */
    fun px2dp(px: Int): Int {
        val density = App.context.resources.displayMetrics.density
        return (px.toFloat() / density).toInt()
    }

    val isLocaleJp: Boolean
        get() {
            val config = App.context.resources.configuration
            return config.locale != null && Locale.JAPAN == config.locale
        }

    val isOrientationLandscape: Boolean
        get() = App.context.resources.configuration.orientation === Configuration.ORIENTATION_LANDSCAPE

}
