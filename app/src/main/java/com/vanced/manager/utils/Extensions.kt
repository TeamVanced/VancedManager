package com.vanced.manager.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.util.Log
import android.widget.RadioGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.radiobutton.MaterialRadioButton
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileOutputStream
import com.vanced.manager.R
import java.util.*

fun RadioGroup.getCheckedButtonTag(): String? {
    return findViewById<MaterialRadioButton>(checkedRadioButtonId)?.tag?.toString()
}

fun DialogFragment.show(activity: FragmentActivity) {
    try {
        show(activity.supportFragmentManager, "")
    } catch (e: Exception) {
        Log.d("VMUI", e.stackTraceToString())
    }

}

fun List<String>.convertToAppVersions(): List<String> = listOf("latest") + reversed()

fun String.convertToAppTheme(context: Context): String {
    return context.getString(R.string.light_plus_other, this.capitalize(Locale.ROOT))
}

fun String.getLatestAppVersion(versions: List<String>): String = if (this == "latest") versions.reversed()[0] else this

fun Context.lifecycleOwner(): LifecycleOwner? {
    var curContext = this
    var maxDepth = 20
    while (maxDepth-- > 0 && curContext !is LifecycleOwner) {
        curContext = (curContext as ContextWrapper).baseContext
    }
    return if (curContext is LifecycleOwner) {
        curContext
    } else {
        null
    }
}

fun Int.toHex(): String = java.lang.String.format("#%06X", 0xFFFFFF and this)

//Material team decided to keep their LinearProgressIndicator final
//At least extension methods exist
fun LinearProgressIndicator.applyAccent() {
    with(accentColor.value ?: context.defPrefs.managerAccent) {
        setIndicatorColor(this)
        trackColor = ColorUtils.setAlphaComponent(this, 70)
    }
}

fun MaterialAlertDialogBuilder.applyAccent() {
    with(accentColor.value ?: context.defPrefs.managerAccent) {
        show().apply {
            getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(this@with)
            getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(this@with)
            getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(this@with)
        }
    }
}

fun Context.writeServiceDScript(apkFPath: String, path: String, app: String) {
    val shellFileZ = SuFile.open("/data/adb/service.d/$app.sh")
    shellFileZ.createNewFile()
    val code = """#!/system/bin/sh${"\n"}while [ "`getprop sys.boot_completed | tr -d '\r' `" != "1" ]; do sleep ${defPrefs.serviceDSleepTimer}; done${"\n"}chcon u:object_r:apk_data_file:s0 $apkFPath${"\n"}mount -o bind $apkFPath $path"""
    SuFileOutputStream(shellFileZ).use { out ->  out.write(code.toByteArray())}
}
