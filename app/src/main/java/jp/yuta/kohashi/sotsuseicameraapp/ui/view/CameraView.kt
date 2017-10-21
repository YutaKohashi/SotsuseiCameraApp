package jp.yuta.kohashi.sotsuseicameraapp.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.hardware.camera2.*
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraAccessException
import android.view.Surface
import android.view.SurfaceHolder
import android.hardware.camera2.CameraCaptureSession


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

    private var mCameraAccessExceptionCallback: (() -> Unit)? = null
    private var mNoCameraPermissionCallback: (() -> Unit)? = null


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("CameraView","onAttachedToWindow")
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            var backCameraId: String? = null

            manager.cameraIdList?.
                    filter { manager.getCameraCharacteristics(it).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK }?.
                    forEach { backCameraId = it }

            backCameraId?.let {
                try {
                    holder.setFixedSize(width, height);

                    manager.openCamera(it, OpenCameraCallback(), null)
                } catch (e: SecurityException) {
                    mNoCameraPermissionCallback?.invoke(); return@let
                }
            } ?: mCameraAccessExceptionCallback?.invoke();return

        } catch (e: CameraAccessException) {
            //例外処理を記述
            mCameraAccessExceptionCallback?.invoke()
        }
    }

    fun setOnCameraAccessExceptionCallback(callback: () -> Unit) {
        mCameraAccessExceptionCallback = callback
    }

    fun setOnNoCameraPermissionCallback(callback: () -> Unit) {
        mNoCameraPermissionCallback = callback
    }

    fun startPreview() {

    }

    fun stopPreview() {

    }


    fun getPreviewBitmap(callback: (Bitmap) -> Unit){
//        callback.invoke(Bitmap())
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
    }


    // region inner classes

    private inner class OpenCameraCallback : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            mBackCameraDevice = cameraDevice

            // プレビュー用のSurfaceViewをリストに登録
            val surfaceList = mutableListOf(holder.surface)

            try {
                // プレビューリクエストの設定（SurfaceViewをターゲットに）
                mPreviewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                mPreviewRequestBuilder?.addTarget(holder.surface)

                // キャプチャーセッションの開始(セッション開始後に第2引数のコールバッククラスが呼ばれる)
                cameraDevice.createCaptureSession(surfaceList, CameraCaptureSessionCallback(), null)

            } catch (e: CameraAccessException) {
                // エラー時の処理を記載
                mCameraAccessExceptionCallback?.invoke()
            }
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            // 切断時の処理を記載
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            // エラー時の処理を記載
        }
    }

    private inner class CameraCaptureSessionCallback : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession?) {
        }

        override fun onConfigured(session: CameraCaptureSession?) {
            mBackCameraSession = session
            try {
                mPreviewRequestBuilder?.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START)
                session?.setRepeatingRequest(mPreviewRequestBuilder?.build(), CaptureCallback(), null)
            } catch (e: CameraAccessException) {
                Log.e("CameraView", "failure stop repeating\n" + e.toString())
                mCameraAccessExceptionCallback?.invoke()
            }
        }

    }


    /**
     * カメラ撮影時に呼ばれるコールバック関数
     */
    private inner class CaptureCallback : CameraCaptureSession.CaptureCallback()

    // endregion
}