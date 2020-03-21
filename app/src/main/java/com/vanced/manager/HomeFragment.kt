package com.vanced.manager

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.content.pm.PackageManager
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import android.widget.Toast
import androidx.core.content.ContextCompat

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun isMicrogInstalled(packageName:String, packageManager:PackageManager):Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e:PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).supportActionBar?.title = getString(R.string.home)

        val braveurl = "https://brave.com/van874"
        val vancedurl = "https://vanced.app"
        val builder = CustomTabsIntent.Builder()
        val brave = getView()?.findViewById(R.id.button) as Button
        val website = getView()?.findViewById(R.id.button2) as Button
        val mgsettings = getView()?.findViewById(R.id.button8) as Button
        val pm = activity?.packageManager
        val isInstalled = pm?.let { isMicrogInstalled("com.mgoogle.android.gms", it) }

        super.onViewCreated(view, savedInstanceState)

        brave.setOnClickListener{
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Brave))
           val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(braveurl))
        }
        website.setOnClickListener{
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Vanced))
           val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(vancedurl))
        }
        mgsettings.setOnClickListener{
            if (isInstalled == true) {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.mgoogle.android.gms",
                    "org.microg.gms.ui.SettingsActivity"
                )
                startActivity(intent)
            }
            else {
                val toast = Toast.makeText(context, "Install MicroG First!", Toast.LENGTH_SHORT)
                toast.show()
            }

        }
    }

}
