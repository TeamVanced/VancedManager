package com.vanced.manager.core.ui.ext

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

fun Fragment.requireSupportFM() = requireActivity().supportFragmentManager

fun <D : DialogFragment> Fragment.showDialog(dialog: D) {
	dialog.show(requireSupportFM(), dialog::class.simpleName)
}