package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.os.Handler
import java.util.*
import kotlin.concurrent.timer

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 17 / 10 / 2017
 */
class RegularlyScheduler private constructor(val period: Long, val initialDelay: Long, val job: () -> Unit) {

    private var mTimer: Timer? = null
    private var mHandler: Handler? = null

    private var running: Boolean = false

    companion object {
        fun build(init: Builder.() -> Unit) = Builder(init).build()
    }

    private constructor(builder: Builder) : this(builder.periodTime, builder.initialDelayTime, builder.job)

    /**
     * タイマータスク
     */
    private fun scheduleAction() {
        mHandler = Handler()
        mTimer = Timer()
        timer(initialDelay = initialDelay, period = period) {
            mHandler?.post {
                job.invoke()
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

    fun onPause() {
        mTimer?.cancel()
        mTimer = null
        mHandler = null
    }

    //region Builder

    class Builder private constructor() {

        var periodTime: Long = 0
        var initialDelayTime: Long = 0
        lateinit var job: () -> Unit

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        fun periodTime(action: Builder.() -> Long) = apply { periodTime = action() }

        fun initialDelayTime(action: Builder.() -> Long) = apply { initialDelayTime = action() }

        fun job(action: Builder.() -> (() -> Unit)) = apply { job = action() }

        fun build(): RegularlyScheduler = RegularlyScheduler(this)

    }

    //endregion
}
