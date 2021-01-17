package com.vanced.manager.core.ui.ext

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

fun Fragment.requireSupportFM() = requireActivity().supportFragmentManager

fun <D : DialogFragment> Fragment.showDialog(dialog: D) {
	try {
		dialog.show(requireSupportFM(), dialog::class.simpleName)
	} catch (e: IllegalStateException) {
		Log.d("VMUI", "Can not perform this action after onSaveInstanceState")
	}

}