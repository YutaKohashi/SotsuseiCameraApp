package jp.yuta.kohashi.sotsuseicameraapp.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.hardware.camera2.CameraAccessException
import android.media.ImageReader
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView


/**
 * Author : yuta
 * Project name : SotsuseiCameraApp
 * Date : 19 / 10 / 2017
 */
class CameraView : SurfaceView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mBackCameraDevice: CameraDevice? = null
    private var mBackCameraSession: CameraCaptureSession? = null
    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    private var mPreviewRequest: CaptureRequest? = null

    private var mCameraAccessExceptionCallback: (() -> Unit)? = null
    private var mNoCameraPermissionCallback: (() -> Unit)? = null

    private var mImageReader: ImageReader? = null
    private val IMAGE_WIDTH = 320
    private val IMAGE_HEIGHT = 240
    private val MAX_IMAGES = 5

    private var mBackgroundHandler = Handler()
    private var mLatestBmp: Bitmap? = null


    fun setOnCameraAccessExceptionCallback(callback: () -> Unit) {
        mCameraAccessExceptionCallback = callback
    }

    fun setOnNoCameraPermissionCallback(callback: () -> Unit) {
        mNoCameraPermissionCallback = callback
    }


    fun getPreviewBitmap(callback: (Bitmap?) -> Unit) {
        callback.invoke(mLatestBmp)
    }

    fun getPreviewNonNullBitmap(callback: (Bitmap) -> Unit) {
        mLatestBmp?.let { callback.invoke(it) }
    }

    /**
     * ImageReader
     */
    private val mImageListener = ImageReader.OnImageAvailableListener { imageReader ->

        fun imageReader2bmp(imageReader: ImageReader): Bitmap? {
            val imageBytes = imageReader.acquireLatestImage()?.run {
                val imageBuf = planes[0].buffer
                val imageBytes = ByteArray(imageBuf.remaining())
                imageBuf.get(imageBytes)
                close()
                imageBytes
            }
            return imageBytes?.let { BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size) }
        }

//        val before = mLatestBmp
        mLatestBmp = imageReader2bmp(imageReader)
//        before?.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("CameraView", "onAttachedToWindow")

        mImageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, MAX_IMAGES);
        mImageReader?.setOnImageAvailableListener(mImageListener, mBackgroundHandler);

        try {
            openCamera()
        } catch (e: CameraAccessException) {
            Log.d("CameraView", "CameraAccessException")
            mNoCameraPermissionCallback?.invoke()
        } catch (e: SecurityException) {
            Log.d("CameraView", "SecurityException")
            mCameraAccessExceptionCallback?.invoke()
//            throw SecurityException(e)
        }
    }

    /**
     * 実装元のActivityまたはFragmentのonPauseメソッドで実行する
     */
    fun onPause() {
        mBackCameraSession?.let {
            try {
                it.stopRepeating()
            } catch (e: CameraAccessException) {
                Log.e("CameraView", "failure stop repeating\n" + e.toString())
                mCameraAccessExceptionCallback?.invoke()
            }
            it.close()
            mBackCameraDevice?.close()
        }
        mLatestBmp?.recycle()
    }

    @Throws(SecurityException::class, CameraAccessException::class)
    private fun openCamera() {
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var backCameraId: String? = null
        manager.cameraIdList?.
                filter { manager.getCameraCharacteristics(it).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK }?.
                forEach { backCameraId = it }
        backCameraId?.let {
            holder.setFixedSize(width, height);
            manager.openCamera(it, OpenCameraCallback(), null)
        } ?: throw CameraAccessException(CameraAccessException.CAMERA_ERROR)

//        val characteristics = manager.getCameraCharacteristics(backCameraId)
//        val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
////        val largest = Collections.max(map.getOutputSizes(ImageFormat.JPEG).asList(), CompareSizesByArea())
////        mImageReader = ImageReader.newInstance(largest.width, largest.height, ImageFormat.JPEG, MAX_IMAGES);
////        mImageReader?.setOnImageAvailableListener(mImageListener, mBackgroundHandler);
    }


//    internal class CompareSizesByArea : Comparator<Size> {
//        // We cast here to ensure the multiplications won't overflow
//        override fun compare(lhs: Size, rhs: Size): Int =
//                java.lang.Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
//
//    }


    // region inner classes

    private inner class OpenCameraCallback : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            createCameraSession(cameraDevice)
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            // 切断時の処理を記載
            mBackCameraDevice?.close()
            mBackCameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            // エラー時の処理を記載
            mBackCameraDevice?.close()
            mBackCameraDevice = null
        }

        /**
         *
         */
        private fun createCameraSession(cameraDevice: CameraDevice) {
            mBackCameraDevice = cameraDevice

            // プレビュー用のSurfaceViewをリストに登録
            val surfaceList = mutableListOf(holder.surface, mImageReader?.surface)

            try {
                // プレビューリクエストの設定（SurfaceViewをターゲットに）
                mPreviewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                mPreviewRequestBuilder?.addTarget(holder.surface)
                mPreviewRequestBuilder?.addTarget(mImageReader?.surface)
                mPreviewRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO)
                mPreviewRequestBuilder?.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START)
                mPreviewRequest = mPreviewRequestBuilder?.build()
                // キャプチャーセッションの開始(セッション開始後に第2引数のコールバッククラスが呼ばれる)
                cameraDevice.createCaptureSession(surfaceList, CameraCaptureSessionCallback(), null)

            } catch (e: CameraAccessException) {
                // エラー時の処理を記載
                mCameraAccessExceptionCallback?.invoke()
            }
        }

        private inner class CameraCaptureSessionCallback : CameraCaptureSession.StateCallback() {
            override fun onConfigureFailed(session: CameraCaptureSession?) {
            }

            override fun onConfigured(session: CameraCaptureSession?) {
                if (mBackCameraDevice == null) return

                mBackCameraSession = session
                try {
                    session?.setRepeatingRequest(mPreviewRequest, CaptureCallback(), null)
                } catch (e: CameraAccessException) {
                    Log.e("CameraView", "failure stop repeating\n" + e.toString())
                    mCameraAccessExceptionCallback?.invoke()
                }
            }

        }
    }


    /**
     * カメラ撮影時に呼ばれるコールバック関数
     */
    private inner class CaptureCallback : CameraCaptureSession.CaptureCallback()

    // endregion
}