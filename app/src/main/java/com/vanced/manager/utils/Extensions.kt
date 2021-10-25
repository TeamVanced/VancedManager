package com.vanced.manager.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.widget.RadioGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.radiobutton.MaterialRadioButton
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileOutputStream
import com.vanced.manager.R
import com.vanced.manager.utils.AppUtils.log
import java.util.*

val RadioGroup.checkedButtonTag: String?
    get() = findViewById<MaterialRadioButton>(
        checkedRadioButtonId
    )?.tag?.toString()

fun DialogFragment.show(fragmentManager: FragmentManager) {
    try {
        show(fragmentManager, "")
    } catch (e: Exception) {
        log("VMUI", e.stackTraceToString())
    }
}

fun DialogFragment.show(activity: FragmentActivity) {
    show(activity.supportFragmentManager)
}

fun List<String>.convertToAppVersions(): List<String> = listOf("latest") + reversed()

fun String.formatVersion(context: Context): String =
    if (this == "latest") context.getString(R.string.install_latest) else this

fun String.convertToAppTheme(context: Context): String = with(context) {
    getString(
        R.string.light_plus_other,
        if (this@convertToAppTheme == "dark") getString(R.string.vanced_dark) else getString(R.string.vanced_black)
    )
}

fun String.getLatestAppVersion(versions: List<String>): String =
    if (this == "latest") versions.reversed()[0] else this

val Context.lifecycleOwner: LifecycleOwner?
    get() = when (this) {
        is LifecycleOwner -> this
        !is LifecycleOwner -> (this as ContextWrapper).baseContext as LifecycleOwner
        else -> null
    }

fun Int.toHex(): String = java.lang.String.format("#%06X", 0xFFFFFF and this)

//Material team decided to keep their LinearProgressIndicator final
//At least extension methods exist
fun LinearProgressIndicator.applyAccent() {
    with(accentColor.value!!) {
        setIndicatorColor(this)
        trackColor = ColorUtils.setAlphaComponent(this, 70)
    }
}

fun MaterialAlertDialogBuilder.showWithAccent() {
    with(accentColor.value!!) {
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
    val script = """
        #!/system/bin/sh
        while [ "$(getprop sys.boot_completed | tr -d '\r')" != "1" ]; do sleep 1; done
        sleep ${defPrefs.serviceDSleepTimer}
        chcon u:object_r:apk_data_file:s0 $apkFPath
        mount -o bind $apkFPath $path
    """.trimIndent()
    SuFileOutputStream.open(shellFileZ).use { out -> out.write(script.toByteArray()) }
}
