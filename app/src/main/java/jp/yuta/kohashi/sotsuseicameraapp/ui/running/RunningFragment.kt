package jp.yuta.kohashi.sotsuseicameraapp.ui.running

import android.graphics.Bitmap
import android.graphics.Camera
import android.util.Log
import android.widget.Toast
import com.otaliastudios.cameraview.Audio
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.CameraView
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.SotsuseiApiManager
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.CaptureHelper
import jp.yuta.kohashi.sotsuseicameraapp.ui.RegularlyScheduler
import jp.yuta.kohashi.sotsuseicameraapp.ui.ToastHelper
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

    private var beforeBmp: Bitmap? = null

    /**
     * キャプチャ後のコールバック
     */
    private val callback: (Bitmap) -> Unit = { bitmap ->
        Toast.makeText(activity, "callback", Toast.LENGTH_SHORT).show()
        SotsuseiApiManager.uploadImage(bitmap, "", { model, error, type ->
            if (!error) Log.d(TAG, "success uploaded image")
            else Log.d(TAG, "failure uploaded image")
        })
        beforeBmp?.recycle()
        beforeBmp = bitmap
    }

    override fun setEvent() {
        cameraView.addCameraListener(cameraListener(callback))

        mRegularlyScheduler = RegularlyScheduler(
                { cameraView.captureSnapshot() },
                3000L,
                PERIOD_TIME
        ).start()

//        mRegularlyScheduler = RegularlyScheduler(
//                { CaptureHelper.takeCapture(stopButton,callback)},
//                3000L,
//                PERIOD_TIME
//        ).start()
        stopButton.setOnClickListener {
            mRegularlyScheduler?.stop()
        }
    }

    private fun cameraListener(callback: (Bitmap) -> Unit) = ExtensionCameraListener(callback)

    private class ExtensionCameraListener(private val callback: (Bitmap) -> Unit) : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            jpeg?.let { CameraUtils.decodeBitmap(it, { callback.invoke(it) }) }
        }
    }

    override fun onResume() {
        super.onResume()
        mRegularlyScheduler?.onResume()
        cameraView?.start()
    }

    override fun onPause() {
        super.onPause()
        mRegularlyScheduler?.onPause()
        cameraView?.stop()
        beforeBmp?.recycle()
    }


    override fun onDestroy() {
        super.onDestroy()
        SotsuseiApiManager.dispose()
        cameraView?.destroy()
    }
}