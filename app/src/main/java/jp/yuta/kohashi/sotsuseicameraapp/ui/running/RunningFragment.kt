package jp.yuta.kohashi.sotsuseicameraapp.ui.running

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.SotsuseiApiManager
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.CaptureHelper
import jp.yuta.kohashi.sotsuseicameraapp.ui.RegularlyScheduler
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_running.*

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

class RunningFragment : BaseFragment() {
    private val TAG = RunningFragment.javaClass.simpleName

    companion object {
        private val PERIOD_TIME = 5000L
    }

    override val sLayoutRes: Int
        get() = R.layout.fragment_running

    private var mRegularlyScheduler: RegularlyScheduler? = null

    /**
     * キャプチャ後のコールバック
     */
    private val callback: (Bitmap) -> Unit = { bitmap ->
//        Log.d(TAG,"callback")
        Toast.makeText(activity,"callback",Toast.LENGTH_SHORT).show()

        SotsuseiApiManager.uploadImage(bitmap,"",{ model, error, type ->
            if(!error)Log.d(TAG,"success uploaded image")
            else Log.d(TAG,"failure uploaded image")
        })
         bitmap.recycle()
    }

    override fun setEvent() {

        mRegularlyScheduler = RegularlyScheduler(
                { CaptureHelper.takeCapture(stopButton, callback) },
                3000L,
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
        mRegularlyScheduler?.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        SotsuseiApiManager.dispose()
    }
}