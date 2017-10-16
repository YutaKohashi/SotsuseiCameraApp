package jp.yuta.kohashi.sotsuseicameraapp.ui.login

import android.os.Bundle
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 27 / 09 / 2017
 */
class LoginFragment: BaseFragment(){

    override val sLayoutRes: Int
        get() = R.layout.fragment_login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setEvent() {
        loginButton.setOnClickListener {
            HomeActivity.start(activity)
        }
    }

}