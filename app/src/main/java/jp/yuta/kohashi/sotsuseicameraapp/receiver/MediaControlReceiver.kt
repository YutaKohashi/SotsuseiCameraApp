package jp.yuta.kohashi.sotsuseicameraapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.view.KeyEvent


/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 27 / 09 / 2017
 */
class MediaControlReceiver(action:() -> Unit) : BroadcastReceiver() {
    private val TAG = MediaControlReceiver::class.java.simpleName

    private val action = action

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive")
//        if (Intent.ACTION_MEDIA_BUTTON == intent.action) {
//
//        }
        val event = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_KEY_EVENT) as KeyEvent
        when(event.keyCode){
            KeyEvent.KEYCODE_VOLUME_DOWN ,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_MUTE -> {
                //　押下処理
                action.invoke()
            }
        }
    }
}