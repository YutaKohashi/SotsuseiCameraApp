package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 16 / 10 / 2017
 */

class CaptureManager(context: Context, view: View) {

    private var onCapture: ((Bitmap?) -> Unit)? = null

    private val targetView: View = view
    private val context: Context = context
    private var bmp: Bitmap? = null


    fun execute(callback: ((Bitmap?) -> Unit)) {
        onCapture = callback
        takeCapture()
    }

    private fun takeCapture() {
        targetView.isDrawingCacheEnabled = true      // キャッシュを取得する設定にする
        targetView.destroyDrawingCache()             // 既存のキャッシュをクリアする
        bmp = targetView.drawingCache
        onCapture?.invoke(bmp)
    }

}