package com.vanced.manager.core.ext

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlin.reflect.full.createInstance

fun Fragment.requireSupportFM() = requireActivity().supportFragmentManager

inline fun <reified D : DialogFragment> Fragment.showDialogRefl() {
	D::class.createInstance().show(requireSupportFM(), D::class.simpleName)
}

fun <D : DialogFragment> Fragment.showDialog(dialog: D) {
	dialog.show(requireSupportFM(), dialog::class.simpleName)
}