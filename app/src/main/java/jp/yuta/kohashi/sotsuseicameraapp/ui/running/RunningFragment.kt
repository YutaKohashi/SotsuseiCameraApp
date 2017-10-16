package jp.yuta.kohashi.sotsuseicameraapp.ui.running

import android.graphics.Bitmap
import android.os.Handler
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.CaptureHelper
import kotlinx.android.synthetic.main.fragment_running.*
import java.util.*
import kotlin.concurrent.timer

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

    private var mTimer: Timer? = null
    private var mHandler: Handler? = null

    /**
     * キャプチャ後のコールバック
     */
    val callback: (Bitmap?) -> Unit = { bitmap ->
        bitmap?.let {
            /**
             * TODO:サーバに送信
             */

        }
    }

    override fun setEvent() {

        mHandler = Handler()
        mTimer = Timer()
        timer(initialDelay = 5000L, period = PERIOD_TIME) {
            mHandler?.post { CaptureHelper.takeCapture(surfaceView, callback) }
        }
    }

    override fun onPause() {
        super.onPause()
        mTimer?.cancel()
        mTimer = null
    }

}