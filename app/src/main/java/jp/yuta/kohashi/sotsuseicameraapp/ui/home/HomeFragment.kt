package jp.yuta.kohashi.sotsuseicameraapp.ui.home

import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.running.RunningActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 27 / 09 / 2017
 */

class HomeFragment: BaseFragment(){

    override val sLayoutRes: Int
        get() = R.layout.fragment_home


    override fun setEvent() {
        startButton.setOnClickListener {
            RunningActivity.start(activity)
        }
    }


}