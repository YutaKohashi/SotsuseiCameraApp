package jp.yuta.kohashi.sotsuseicameraapp.ui.running

import android.graphics.Bitmap
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.SotsuseiApiManager
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.CaptureHelper
import jp.yuta.kohashi.sotsuseicameraapp.ui.RegularlyScheduler
import kotlinx.android.synthetic.main.fragment_running.*

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

class RunningFragment : BaseFragment() {

    companion object {
        private val PERIOD_TIME = 3000L
    }

    override val sLayoutRes: Int
        get() = R.layout.fragment_running

    private var mRegularlyScheduler: RegularlyScheduler? = null

    /**
     * キャプチャ後のコールバック
     */
    private val callback: (Bitmap?) -> Unit = { bitmap ->
        bitmap?.let {
            /**
             * TODO:サーバに送信
             */
            SotsuseiApiManager.uploadImage(it, { model, error, type ->
                if (!error) {

                } else {

                }
            })
        }
    }

    override fun setEvent() {

        mRegularlyScheduler = RegularlyScheduler(
                { CaptureHelper.takeCapture(surfaceView, callback) },
                5000L,
                PERIOD_TIME
        ).start()

        stopButton.setOnClickListener {
            mRegularlyScheduler?.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        mRegularlyScheduler?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mRegularlyScheduler?.stop()
    }

}