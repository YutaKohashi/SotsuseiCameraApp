package jp.yuta.kohashi.sotsuseicameraapp.ui.running

//import com.wonderkiln.camerakit.CameraListener
//import com.otaliastudios.cameraview.Audio
//import com.otaliastudios.cameraview.CameraListener
//import com.wonderkiln.camerakit.CameraKit
//import com.otaliastudios.cameraview.CameraView
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import jp.yuta.kohashi.fakelineapp.managers.FileManager
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.SotsuseiApiManager
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.RegularlyScheduler
import kotlinx.android.synthetic.main.fragment_running.*
import kotlin.concurrent.thread

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

class RunningFragment : BaseFragment() {
    private val TAG = RunningFragment::class.java.simpleName

    private val fileManger by lazy { FileManager(context) }


    companion object {
        private val PERIOD_TIME = 3000L
    }

    override val sLayoutRes: Int
        get() = R.layout.fragment_running

    private var mRegularlyScheduler: RegularlyScheduler? = null
    private var count: Int = 0
    /**
     * キャプチャ後のコールバック
     */
    private val callback: (Bitmap) -> Unit = { bitmap ->
        Log.d(TAG, "bitmap callback  :  " + count.toString())
        count += 1

        thread {
            fileManger.deleteFileExternalStorage("/", "image1.jpg")
            fileManger.saveBitmapExternalStorage("/", "image1.jpg", bitmap)
        }
        imageView.setImageBitmap(bitmap)
        thread {
            //            Toast.makeText(activity, "callback", Toast.LENGTH_SHORT).show()
            SotsuseiApiManager.uploadImage(bitmap, "", { model, error, type ->
                if (!error) Log.d(TAG, "success uploaded image")
                else Log.d(TAG, "failure uploaded image")
            })
        }
    }

    override fun setEvent() {
//        cameraView.addCameraListener(cameraListener(callback))
//
//        mRegularlyScheduler = RegularlyScheduler(
//                { cameraView.captureSnapshot() },
//                3000L,
//                PERIOD_TIME
//        ).start()

//        cameraView.setCameraListener(cameraListener(callback))
//        cameraView.setMethod(CameraKit.Constants.METHOD_STILL)
//        mRegularlyScheduler = RegularlyScheduler(
//                { cameraView.captureImage() },
//                3000L,
//                PERIOD_TIME
//        ).start()

        mRegularlyScheduler = RegularlyScheduler(
                { cameraView.getPreviewNonNullBitmap(callback) },
                3000L,
                PERIOD_TIME
        ).start()
        stopButton.setOnClickListener {
            mRegularlyScheduler?.stop()
        }
    }

//
//    private fun cameraListener(callback: (Bitmap) -> Unit) = ExtensionCameraListener(callback)
//
//    private class ExtensionCameraListener(private val callback: (Bitmap) -> Unit) : CameraListener() {
//        override fun onPictureTaken(jpeg: ByteArray?) {
//            jpeg?.let { CameraUtils.decodeBitmap(it, { callback.invoke(it) }) }
//        }
//    }

    override fun onResume() {
        super.onResume()
        mRegularlyScheduler?.onResume()
//        cameraView?.start()

    }

    override fun onPause() {
        super.onPause()
        mRegularlyScheduler?.onPause()
        cameraView?.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        SotsuseiApiManager.dispose()
//        cameraView?.destroy()
    }
}