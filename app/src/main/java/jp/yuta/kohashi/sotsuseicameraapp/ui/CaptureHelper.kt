package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.graphics.Bitmap
import android.os.Handler
import android.view.View
import kotlin.concurrent.thread

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 16 / 10 / 2017
 */

object CaptureHelper {

    /**
     * targetViewに指定されたViewのキャプチャを取得する
     * callbackでbitmapが帰ってくる
     */
    fun takeCapture(targetView: View, callback: ((Bitmap) -> Unit)) {
        val h = Handler()
        thread {
            targetView.isDrawingCacheEnabled = true      // キャッシュを取得する設定にする
            targetView.destroyDrawingCache()             // 既存のキャッシュをクリアする
            h.post { targetView.drawingCache?.let{callback.invoke(it)} }
        }
    }

}