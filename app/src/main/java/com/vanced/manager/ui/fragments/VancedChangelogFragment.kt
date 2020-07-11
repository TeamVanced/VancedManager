package com.vanced.manager.ui.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.PackageHelper
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class VancedChangelogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vanced_changelog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = context
        val appContext = activity?.applicationContext
        val application = activity?.application
        runBlocking {
            launch {
                val changelogTxt = view.findViewById<TextView>(R.id.vanced_changelog)

                // Not very clean code, I know.
                // This is also now giving you the changelog of the currently installed version.
                // Should probably give the changelog of the newest version available?
                if (context != null && application != null) {
                    val variant = PreferenceManager.getDefaultSharedPreferences(appContext)
                        .getString("vanced_variant", "nonroot")

                    val vancedPkgName: String =
                        if (variant== "root") {
                            "com.google.android.youtube"
                        } else {
                            "com.vanced.android.youtube"
                        }

                    val vancedInstalled = (PackageHelper.isPackageInstalled(vancedPkgName, application.packageManager))
                    var vancedVersion = getPkgInfo(vancedInstalled, vancedPkgName, application)
                    vancedVersion = vancedVersion.replace('.', '_')

                    InternetTools.getJsonString("vanced.json", "version", context)
                    var baseUrl = PreferenceManager.getDefaultSharedPreferences(context)
                        .getString("install_url", InternetTools.baseUrl)
                    baseUrl = baseUrl?.trimEnd('/')

                    val changelog = InternetTools.getObjectFromJson("$baseUrl/changelog/$vancedVersion.json", "message");
                    changelogTxt.text = changelog
                }
                else {
                    changelogTxt.text = "No changelog..."
                }
            }
        }
    }

    private fun getPkgInfo(toCheck: Boolean, pkg: String, application: Application): String  {
        return if (toCheck) {
            application.packageManager.getPackageInfo(pkg, 0).versionName
        } else {
            application.getString(R.string.unavailable)
        }
    }
}
