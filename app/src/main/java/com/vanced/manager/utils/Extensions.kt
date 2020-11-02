package com.vanced.manager.utils

import android.app.Activity
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R
import com.vanced.manager.core.App

object Extensions {

    fun RadioGroup.getCheckedButtonTag(): String {
        return findViewById<MaterialRadioButton>(checkedRadioButtonId).tag.toString()
    }

    fun DialogFragment.show(activity: FragmentActivity) {
        this.show(activity.supportFragmentManager, "")
    }

    fun Activity.fetchData() {
        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.home_refresh)
        setRefreshing(true, refreshLayout)
        (application as App).loadJson()
        setRefreshing(false, refreshLayout)
    }

    fun Activity.setRefreshing(isRefreshing: Boolean) {
        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.home_refresh)
        if (refreshLayout != null) {
            refreshLayout.isRefreshing = isRefreshing
        }
    }

    fun Activity.setRefreshing(isRefreshing: Boolean, refreshLayout: SwipeRefreshLayout?) {
        if (refreshLayout != null) {
            refreshLayout.isRefreshing = isRefreshing
        }
    }

}