package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.os.Handler
import java.util.*
import kotlin.concurrent.timer

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 17 / 10 / 2017
 */
class RegularlyScheduler {

    private val action: () -> Unit
    private var mTimer: Timer? = null
    private var mHandler: Handler? = null

    var initialDelay: Long = 0
    var period: Long = 0

    private var running: Boolean = false

    constructor(action: () -> Unit) : this(action, 0, 0)

    constructor(action: () -> Unit, initialDelay: Long, period: Long) {
        this.action = action
        this.initialDelay = initialDelay
        this.period = period
    }

    /**
     * タイマータスク
     */
    private fun scheduleAction() {
        mHandler = Handler()
        mTimer = Timer()
        timer(initialDelay = initialDelay, period = period) {
            mHandler?.post {
                action.invoke()
            }
        }
    }

    fun start(): RegularlyScheduler {
        scheduleAction()
        running = true
        return this
    }

    fun stop() {
        running = false
        mTimer?.cancel()
        mTimer = null
        mHandler = null
    }

    fun restart() {
        scheduleAction()
    }

    /**
     * onResumeで使用
     */
    fun onResume() {
        if (running && mTimer == null) scheduleAction()
    }
}
