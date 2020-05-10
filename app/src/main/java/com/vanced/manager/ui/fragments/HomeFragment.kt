package com.vanced.manager.ui.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.animation.addListener
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vanced.manager.R
import com.vanced.manager.adapter.SectionPageAdapter
import com.vanced.manager.core.fragments.Home

class HomeFragment : Home() {

    private lateinit var sectionPageAdapter: SectionPageAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.title_home)
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        connectionStatus()

        super.onViewCreated(view, savedInstanceState)

        sectionPageAdapter = SectionPageAdapter(this)
        val tabLayout = view.findViewById(R.id.tablayout) as TabLayout
        viewPager = view.findViewById(R.id.viewpager)
        viewPager.adapter = sectionPageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Vanced"
                1 -> tab.text = "MicroG"
            }
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super .onCreateOptionsMenu(menu, inflater)
    }

    private var networkCallback = object: ConnectivityManager.NetworkCallback() {

        override fun onLost(network: Network) {
            super.onLost(network)

            activity?.runOnUiThread {

                val networkErrorLayout = view?.findViewById<MaterialCardView>(R.id.home_network_wrapper)
                val mainLayout = view?.findViewById<LinearLayout>(R.id.home_main_wrapper)
                val oa0 = ObjectAnimator.ofFloat(mainLayout, "yFraction", 0f, 0.1f)
                val oa1 = ObjectAnimator.ofFloat(mainLayout, "yFraction", 0.1f, 0.01f)
                val oa2 = ObjectAnimator.ofFloat(networkErrorLayout, "yFraction", -1f, 0.3f)
                val oa3 = ObjectAnimator.ofFloat(networkErrorLayout, "yFraction", 0.3f, 0f)


                oa2.apply {
                    oa2.addListener(onStart = {
                        networkErrorLayout?.visibility = View.VISIBLE
                        oa0.start()
                    }
                    )
                    oa2.addListener(onEnd = {
                        oa1.start()
                    })
                    start()
                }
                oa3.start()

            }

        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            activity?.runOnUiThread {

                val networkErrorLayout = view?.findViewById<MaterialCardView>(R.id.home_network_wrapper)
                val mainLayout = view?.findViewById<LinearLayout>(R.id.home_main_wrapper)
                val oa0 = ObjectAnimator.ofFloat(mainLayout, "yFraction", 0f, 0.1f)
                val oa1 = ObjectAnimator.ofFloat(mainLayout, "yFraction", 0.1f, 0f)
                val oa2 = ObjectAnimator.ofFloat(networkErrorLayout, "yFraction", 0f, 0.3f)
                val oa3 = ObjectAnimator.ofFloat(networkErrorLayout, "yFraction", 0.3f, -1f)

                oa2.start()
                oa3.apply {
                    oa3.addListener(onStart = {
                        oa0.start()
                    })

                    oa3.addListener(onEnd = {
                        oa1.start()
                        networkErrorLayout?.visibility = View.GONE
                    }
                    )
                    start()
                }

            }

        }

    }

    private fun connectionStatus() {
        val connectivityManager = context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {}
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

}

