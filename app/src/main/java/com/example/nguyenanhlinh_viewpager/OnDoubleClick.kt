package com.example.nguyenanhlinh_viewpager

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View



abstract class OnDoubleClick : View.OnClickListener {
    private val doubleClickTimeout: Long = DEFAULT
    private val handler: Handler
    private var isSingleEvent = false
    private var firstClickTime: Long
    private val runnable : Runnable
    override fun onClick(v: View?) {
        val now = SystemClock.elapsedRealtime()
        if (now - firstClickTime < doubleClickTimeout) {
            isSingleEvent = false
            handler.removeCallbacksAndMessages(runnable)
            onDoubleClick()
            return
        } else {
            isSingleEvent = true
            handler.postDelayed(runnable, DEFAULT)
            firstClickTime = SystemClock.elapsedRealtime()
        }
    }
    abstract fun onDoubleClick()
    abstract fun onSingleClick()

    init {
        firstClickTime = 0L
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            if (isSingleEvent) {
                onSingleClick()
            }
        }
    }

    companion object {
        private const val DEFAULT: Long = 250
    }
}