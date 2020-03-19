package com.vanced.manager

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsService
import androidx.browser.customtabs.CustomTabColorSchemeParams
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val braveurl = "https://brave.com/van874"
        val vancedurl = "vanced.app"
        val builder = CustomTabsIntent.Builder()
        val brave = getView()?.findViewById(R.id.button) as Button
        val website = getView()?.findViewById(R.id.button2) as Button

        super.onViewCreated(view, savedInstanceState)

        brave.setOnClickListener{
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Vanced))
           val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(braveurl))
        }
        website.setOnClickListener{
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Brave))
           val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(vancedurl))
        }
    }

}
