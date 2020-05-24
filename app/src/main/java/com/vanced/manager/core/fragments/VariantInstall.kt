package com.vanced.manager.core.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class VariantInstall : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nextButton = view.findViewById<Button>(R.id.vanced_next_to_language)
        val loadBar = view.findViewById<ProgressBar>(R.id.vanvariant_progress)

        nextButton.setOnClickListener {
            val arch =
                when {
                    Build.SUPPORTED_ABIS.contains("x86") -> "x86"
                    Build.SUPPORTED_ABIS.contains("arm64-v8a") -> "arm64-v8a"
                    Build.SUPPORTED_ABIS.contains("armeabi-v7a") -> "armeabi-v7a"
                    else -> "armeabi-v7a"
                }

            downloadSplit("arch", arch, false, loadBar, R.id.toInstallThemeFragment)
        }
    }
}