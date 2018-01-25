package jp.yuta.kohashi.sotsuseicameraapp.ui.running

import android.graphics.Bitmap
import android.util.Log
import jp.yuta.kohashi.fakelineapp.managers.FileManager
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.SotsuseiApiHelper
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.exception.ApiException
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.RegularlyScheduler
import jp.yuta.kohashi.sotsuseicameraapp.utils.PrefUtil
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
        //        private val PERIOD_TIME = 2500L
        private val PERIOD_TIME = if (PrefUtil.debug) PrefUtil.debugCameraCaptureSpan else 25000000000L
    }

    override val sLayoutRes: Int
        get() = R.layout.fragment_running

    private var mRegularlyScheduler: RegularlyScheduler? = null
    private var count: Int = 0

    /**
     * キャプチャ後のコールバック
     */
    private val callback: (Bitmap) -> Unit = { bitmap ->
        Log.d(TAG, "bitmap callback  :  " + (count++).toString())
        imageView.release()
//        thread {
//            //            fileManger.deleteFileExternalStorage("/", "image1.jpg")
////            fileManger.saveBitmapExternalStorage("/", "image1.jpg", previewBmp)
//        }
        imageView.setImageBitmap(bitmap)
        thread {
            SotsuseiApiHelper.uploadImage(bitmap, PrefUtil.storeId, { res, type ->
                if (type == ApiException.ErrorType.ERROR_NO) Log.d(TAG, "success uploaded image")
                else Log.d(TAG, "failure uploaded image")
            })
        }
    }

    override fun setEvent() {
        mRegularlyScheduler = RegularlyScheduler.Builder {
            periodTime = PERIOD_TIME
            initialDelayTime = 3000L
            job = { cameraView.getPreviewNonNullBitmap(callback) }
        }.build().start()

        stopButton.setOnClickListener {
            activity.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mRegularlyScheduler?.onResume()
        cameraView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mRegularlyScheduler?.onPause()
        cameraView?.onPause()
        imageView?.release()
    }


    override fun onDestroy() {
        super.onDestroy()
        SotsuseiApiHelper.dispose()
    }
}