package jp.yuta.kohashi.sotsuseicameraapp.ui.debug

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import jp.yuta.kohashi.sotsuseicameraapp.App


/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 21 / 01 / 2018
 */
class DebugClass(private val mContext: Context, private val callback: () -> Unit) : View.OnTouchListener, Runnable {
    private val TAG = DebugClass::class.java.simpleName

    companion object {
        private val LONG_PRESS_TIME: Long = 2500
    }

    private var isExecute: Boolean = false
    private val mLongPressHandler: Handler


    init {
        isExecute = false
        mLongPressHandler = Handler()
    }

    /**
     * 長押し成功
     */
    override fun run() {
        Toast.makeText(App.context, "Debugging function", Toast.LENGTH_SHORT).show()
        lognTapEvent()
    }

    /**
     * ロングタップイベント
     */
    private fun lognTapEvent() {
        isExecute = true
        callback.invoke()
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                isExecute = false
                mLongPressHandler.postDelayed(this, LONG_PRESS_TIME)
            }
            MotionEvent.ACTION_UP -> {
                mLongPressHandler.removeCallbacks(this)
                if (isExecute) {
                    try {
                        view.getRootView().requestFocus()
                    } catch (ex: Exception) {
                        Log.d("DebugClass", ex.toString())
                    }

                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
            }
            else -> {
            }
        }
        return false
    }


    //**
    //endregion
    //**
}