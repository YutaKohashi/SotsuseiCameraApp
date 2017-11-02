package jp.yuta.kohashi.sotsuseicameraapp.ui.home


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseDrawerActivity
import jp.yuta.kohashi.sotsuseicameraapp.ui.StartActivity
import jp.yuta.kohashi.sotsuseicameraapp.utils.ResUtil

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 29 / 09 / 2017
 */

class HomeActivity : BaseDrawerActivity() {

    companion object : StartActivity<HomeActivity> {
        override fun start(activity: Activity) = super.start(activity, HomeActivity::class.java)
    }

    override val containerFragment: Fragment? = HomeFragment()

    override val menuItemFromRes: Int? = R.menu.menu_drawer

    override val headerViewFromRes: Int? = R.layout.header_drawer

    private lateinit var mNavigationButton: View

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState,false)
        mNavigationButton = defaultDrawerToggle()

        mContainerView.addView(mNavigationButton)
        mContainerView.bringChildToFront(mNavigationButton)
        mContainerView.requestLayout()

        setEvent()
    }

    override fun setEvent() {
        mNavigationView.setBackgroundColor(ResUtil.color(R.color.bg_main))


        mNavigationButton.setOnClickListener {
            openDrawer()
        }
    }
}