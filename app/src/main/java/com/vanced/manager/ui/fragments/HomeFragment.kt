package com.vanced.manager.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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

                val animationShow: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.view_enter)
                val networkErrorLayout = view?.findViewById<MaterialCardView>(R.id.home_network_wrapper)

                networkErrorLayout?.visibility = View.VISIBLE
                networkErrorLayout?.startAnimation(animationShow)

            }

        }

        override fun onUnavailable() {
            super.onUnavailable()

            activity?.runOnUiThread {

                val networkErrorLayout = view?.findViewById<MaterialCardView>(R.id.home_network_wrapper)
                networkErrorLayout?.visibility = View.VISIBLE

            }

        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            activity?.runOnUiThread {

                val animationHide: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.view_exit)
                val networkErrorLayout = view?.findViewById<MaterialCardView>(R.id.home_network_wrapper)

                networkErrorLayout?.startAnimation(animationHide)
                networkErrorLayout?.visibility = View.GONE
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
        } catch (e: Exception) {

        }
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

}

