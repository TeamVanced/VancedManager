package com.vanced.manager.utils

import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.radiobutton.MaterialRadioButton

object Extensions {

    fun RadioGroup.getCheckedButtonTag(): String {
        return findViewById<MaterialRadioButton>(checkedRadioButtonId).tag.toString()
    }

    fun DialogFragment.show(activity: FragmentActivity) {
        this.show(activity.supportFragmentManager, "")
    }

}