package jp.yuta.kohashi.sotsuseicameraapp.ui.running

import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_running.*

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

class RunningFragment: BaseFragment(){

    override val sLayoutRes: Int
        get() = R.layout.fragment_running

    override fun setEvent() {

        /**
         * シャッタボタン
         */
        shutterButton.setOnClickListener {

        }

        /**
         * 違法駐車ボタン
         */
        ihoChushaButton.setOnClickListener {

        }
        /**
         * ストップボタン
         */
        stopButton.setOnClickListener {
//            when(SotsuseiClientAppService.stop(SotsuseiClientAppService::class.java)){
//                StateResult.SUCCESS_STOP -> ToastHelper.stopService()
//                StateResult.ALREADY_STOPPED -> ToastHelper.alreadyStopService()
//            }
        }


    }

}