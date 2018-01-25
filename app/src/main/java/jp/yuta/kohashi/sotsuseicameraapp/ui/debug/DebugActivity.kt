package jp.yuta.kohashi.sotsuseicameraapp.ui.debug

import android.app.Activity
import android.widget.Toast
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.StartActivity
import jp.yuta.kohashi.sotsuseicameraapp.utils.PrefUtil
import kotlinx.android.synthetic.main.activity_debug.*

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 14 / 12 / 2017
 */

class DebugActivity:BaseActivity(){

    override val contentViewFromRes: Int? = R.layout.activity_debug

    companion object :StartActivity<DebugActivity>{
        override fun start(activity: Activity) = start(activity,DebugActivity::class.java)
    }

    override fun setEvent() {
        debugSwitch.isChecked = PrefUtil.debug
        proxySwitch.isChecked = PrefUtil.debugProxy
        cameraSpan.setText(PrefUtil.debugCameraCaptureSpan.toString())
        try {
            val ips = PrefUtil.debugServerIp.split(".")
            ip1.setText(ips[0])
            ip2.setText(ips[1])
            ip3.setText(ips[2])
            ip4.setText(ips[3])
        }catch (e:Exception){
            Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show()
        }

        port.setText(PrefUtil.debugServerPort)

        updateButton.setOnClickListener{
            PrefUtil.debug = debugSwitch.isChecked
            PrefUtil.debugProxy = proxySwitch.isChecked
            PrefUtil.debugServerPort = port.text.toString()
            PrefUtil.debugCameraCaptureSpan = cameraSpan.text.toString().toLong()
            PrefUtil.debugServerIp = ip1.text.toString() + "." + ip2.text.toString() + "." + ip3.text.toString() + "." + ip4.text.toString()
        }
    }
}