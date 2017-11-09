package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.utils.DisplayUtil
import jp.yuta.kohashi.sotsuseicameraapp.utils.Util

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 30 / 10 / 2017
 */
abstract class BaseDrawerActivity : BaseActivity() {
    private val TAG = BaseDrawerActivity::class.java.simpleName

    /**
     * 使用しない
     */
    override val contentViewFromRes: Int? = null
    override var fragment: Fragment? = null

    protected lateinit var mDrawerLayout: DrawerLayout
    protected lateinit var mContainerView: FrameLayout
    protected lateinit var mNavigationView: NavigationView

    /**
     * メニュー項目
     */
    @MenuRes
    open protected val menuItemFromRes: Int? = null

    /**
     * レイアウトリソースを設置するとき
     */
    @LayoutRes
    open protected val headerViewFromRes: Int? = null

    /**
     * Viewを設置するとき
     */
    open protected val headerViewFromView: View? = null

    /**
     * フラグメントを設置するとき
     * fragment  current displayed
     */
    open protected val containerFragment: Fragment? = null

    /**
     * レイアウトリソースを設置するとき
     */
    @LayoutRes
    open protected val containerViewFromRes: Int? = null

    /**
     * Viewを設置するとき
     */
    open protected val containerViewFromView: View? = null


    /**
     * NavigationViewを適用したviewgroup
     */
    override val contentViewFromView: View? by lazy { createContentView() }

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        onCreate(savedInstanceState, false)
    }

    override fun onCreate(savedInstanceState: Bundle?, isEvent: Boolean) {
        super.onCreate(savedInstanceState, isEvent)

        containerViewFromRes?.let { mContainerView.addView(Util.layoutRes2View(this, it, mContainerView, true)) }
        containerViewFromView?.let { mContainerView.addView(it) }
        containerFragment?.let { supportFragmentManager.beginTransaction().apply { add(mContainerView.id, it) }.commit() }
        if (isEvent) setEvent()
    }

    /**
     * create layout by navigation drawer
     */
    private fun createContentView(): View {

        mDrawerLayout = DrawerLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fitsSystemWindows = true
        }
        mContainerView = FrameLayout(this).apply {
            id = View.generateViewId()
            layoutParams = DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT)
        }

        mNavigationView = NavigationView(this).apply {
            layoutParams = DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.MATCH_PARENT).also { params ->
                params.gravity = Gravity.START
            }

            fitsSystemWindows = false
            headerViewFromRes?.let { addHeaderView(Util.layoutRes2View(this@BaseDrawerActivity, it, this)) }
            headerViewFromView?.let { addHeaderView(it) }
            menuItemFromRes?.let { inflateMenu(it) }

        }

        mDrawerLayout.addView(mContainerView)
        mDrawerLayout.addView(mNavigationView)

        return mDrawerLayout
    }


    fun openDrawer(){
        mDrawerLayout.openDrawer(Gravity.END)
    }

    fun closeDrawer(){
        mDrawerLayout.closeDrawer(Gravity.START)
    }


    override fun replaceFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(mContainerView.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun addFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(mContainerView.id, fragment)
        transaction.addToBackStack("")
        transaction.commit()
    }

    fun addFragment(fragment:Fragment,sharedView:View,transitionName:String){
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.addSharedElement(sharedView,transitionName)
        transaction.addToBackStack("")
        transaction.replace(mContainerView.id, fragment)
        transaction.commit()
    }

    /**
     * デフォルトのdrawertoggleを作成するメソッド
     */
    protected fun defaultDrawerToggle():View{
        return ImageButton(this).apply {
            val dp5 = DisplayUtil.dp2px(5f)
            val dp50 = DisplayUtil.dp2px(50f)
            val dp15 = DisplayUtil.dp2px(15f)

            setPadding(dp5, dp5, dp5, dp5)
            background = getDrawable(R.drawable.bg_btn_drawer_toggle)
            setImageResource(R.drawable.ic_menu)
            scaleType = ImageView.ScaleType.FIT_CENTER
            elevation = DisplayUtil.dp2px(4f).toFloat()
            layoutParams = DrawerLayout.LayoutParams(dp50,dp50).apply { setMargins(dp15,dp15,0,0) }
        }
    }

    /**
     * 戻るボタンの押下イベント
     */
    override fun onBackPressed() {
        currentFragment()?.let {
            /**
             * fragment側でfalseを返すとactivityのonbackpressedイベントは呼ばれない
             */
            if ((it as? BaseFragment)?.onBackPressed() == false) return
            else super.onBackPressed()
        } ?:super.onBackPressed()
    }
}
