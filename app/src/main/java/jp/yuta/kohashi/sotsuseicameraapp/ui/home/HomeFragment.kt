package jp.yuta.kohashi.sotsuseicameraapp.ui.home

import android.os.Handler
import android.os.Looper
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

    override val sLayoutRes: Int = R.layout.fragment_home


    override fun setEvent() {
        startButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                RunningActivity.start(activity)
            }, 500)
        }
    }


    override fun onBackPressed(): Boolean {
        activity.finish()
        return super.onBackPressed()
    }
}