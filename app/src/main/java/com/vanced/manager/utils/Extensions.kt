package com.vanced.manager.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R
import com.vanced.manager.model.AppVersionsModel
import com.vanced.manager.utils.InternetTools.loadJson
import java.util.*

object Extensions {

    fun RadioGroup.getCheckedButtonTag(): String {
        return findViewById<MaterialRadioButton>(checkedRadioButtonId).tag.toString()
    }

    fun DialogFragment.show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, "")
    }

    fun Activity.fetchData() {
        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.home_refresh)
        setRefreshing(true, refreshLayout)
        loadJson(this)
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

    fun Context.getDefaultPrefs(): SharedPreferences = getDefaultSharedPreferences(this)

    //Not sure how much this can affect performance
    //but if anyone can improve this even slightly,
    //feel free to open a PR
    fun List<String>.convertToAppVersions(): Array<AppVersionsModel> {
        val versionsModel = arrayListOf(AppVersionsModel("latest", this[0]))
        for (i in reversed().indices) {
            versionsModel.add(AppVersionsModel(this[i], this[i]))
        }
        return versionsModel.toTypedArray()
    }

    fun String.convertToAppTheme(context: Context): String {
        return context.getString(R.string.light_plus_other, this.capitalize(Locale.ROOT))
    }

}