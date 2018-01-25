package jp.yuta.kohashi.sotsuseicameraapp.ui.info

import android.support.v4.app.Fragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseActivity

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 15 / 01 / 2018
 */
class InfoActivity:BaseActivity(){

    override val fragment: Fragment?
        get() = InfoTenpoFragment()

    override fun setEvent() {

    }
}