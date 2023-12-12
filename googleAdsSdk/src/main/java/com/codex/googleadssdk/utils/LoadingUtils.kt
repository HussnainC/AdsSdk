package com.codex.googleadssdk.utils

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager
import com.codex.googleadssdk.R

object LoadingUtils {
    private var dialog: Dialog? = null
    fun showAdLoadingScreen(context: Activity, layoutId: Int) {
        try {
            if (!context.isFinishing ) {
                val adLoadingView =
                    LayoutInflater.from(context).inflate(layoutId, null, false)
                dialog = Dialog(context, R.style.FullScreenDialog)
                adLoadingView?.let {
                    dialog?.setContentView(it)
                    val layoutParams = WindowManager.LayoutParams()
                    layoutParams.copyFrom(dialog?.window?.attributes)
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                    dialog?.window?.attributes = layoutParams
                    dialog?.setCancelable(false)
                    dialog?.show()
                }
            }

        } catch (ex: Exception) {
            "asdjap".showLog(ex.message.toString())
        }
    }

    fun dismissScreen() {
        try {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
        } catch (ex: java.lang.Exception) {
            "asdjap".showLog(ex.message.toString())
        }
    }
}