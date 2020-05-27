package com.vanced.manager.ui.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.viewpager2.widget.ViewPager2
import com.dezlum.codelabs.getjson.GetJson
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vanced.manager.R
import com.vanced.manager.adapter.SectionPageAdapter
import com.vanced.manager.core.fragments.Home
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeFragment : Home() {

    private lateinit var sectionPageAdapter: SectionPageAdapter
    private lateinit var viewPager: ViewPager2
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.title_home)
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNetworkFun()

        sectionPageAdapter = SectionPageAdapter(this)
        val tabLayout = view.findViewById(R.id.tablayout) as TabLayout
        viewPager = view.findViewById(R.id.viewpager)
        viewPager.adapter = sectionPageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Vanced"
                1 -> tab.text = "MicroG"
                2 -> tab.text = "Manager"
            }
        }.attach()

    }

    private fun initNetworkFun() {
        val pm = activity?.packageManager
        val microgStatus = pm?.let { isPackageInstalled("com.mgoogle.android.gms", it) }
        val vancedStatus = pm?.let { isPackageInstalled("com.vanced.android.youtube", it) }
        val microginstallbtn = view?.findViewById<MaterialButton>(R.id.microg_installbtn)
        val vancedinstallbtn = view?.findViewById<MaterialButton>(R.id.vanced_installbtn)
        val vancedLatestTxt = view?.findViewById<TextView>(R.id.vanced_latest_version)
        val microgLatestTxt = view?.findViewById<TextView>(R.id.microg_latest_version)
        val networkErrorLayout = view?.findViewById<MaterialCardView>(R.id.home_network_wrapper)

        disposable = ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isConnectedToInternet ->
                run {
                    if (isConnectedToInternet) {
                        vancedinstallbtn?.visibility = View.VISIBLE
                        microginstallbtn?.visibility = View.VISIBLE

                        val vancedRemoteVer: String =
                            GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/vanced.json")
                                .get("version").asString
                        val microgRemoteVer: String =
                            GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
                                .get("version").asString
                        vancedLatestTxt?.text = vancedRemoteVer
                        microgLatestTxt?.text = microgRemoteVer

                        if (microgStatus!!) {
                            val microgVer =
                                pm.getPackageInfo("com.mgoogle.android.gms", 0).versionName
                            if (microgRemoteVer > microgVer) {
                                microginstallbtn?.text = activity?.getString(R.string.update)
                                microginstallbtn?.icon =
                                    activity?.getDrawable(R.drawable.ic_cloud_upload_black_24dp)
                            }
                            else if (microgRemoteVer == microgVer) {
                                microginstallbtn?.text =
                                    activity?.getString(R.string.button_installed)
                                microginstallbtn?.icon =
                                    activity?.getDrawable(R.drawable.outline_cloud_done_24)
                            }
                        }

                        if (vancedStatus!!) {
                            val vancedVer =
                                pm.getPackageInfo("com.vanced.android.youtube", 0).versionName
                            if (vancedRemoteVer > vancedVer) {
                                vancedinstallbtn?.text = activity?.getString(R.string.update)
                                vancedinstallbtn?.icon =
                                    activity?.getDrawable(R.drawable.ic_cloud_upload_black_24dp)
                            }
                            else if (vancedRemoteVer == vancedVer) {
                                vancedinstallbtn?.text =
                                    activity?.getString(R.string.button_installed)
                                vancedinstallbtn?.icon =
                                    activity?.getDrawable(R.drawable.outline_cloud_done_24)
                            }
                        }

                        val oa2 = ObjectAnimator.ofFloat(networkErrorLayout, "yFraction", 0f, 0.3f)
                        val oa3 = ObjectAnimator.ofFloat(networkErrorLayout, "yFraction", 0.3f, -1f)

                        oa2.start()
                        oa3.apply {
                            oa3.addListener(onEnd = {
                                networkErrorLayout?.visibility = View.GONE
                            })
                            start()
                        }
                    } else {
                        vancedinstallbtn?.visibility = View.INVISIBLE
                        microginstallbtn?.visibility = View.INVISIBLE

                        vancedLatestTxt?.text = getString(R.string.unavailable)
                        microgLatestTxt?.text = getString(R.string.unavailable)

                        val oa2 = ObjectAnimator.ofFloat(networkErrorLayout, "yFraction", -1f, 0.3f)
                        val oa3 = ObjectAnimator.ofFloat(networkErrorLayout, "yFraction", 0.3f, 0f)

                        oa2.apply {
                            oa2.addListener(onStart = {
                                networkErrorLayout?.visibility = View.VISIBLE
                            })
                            start()
                        }
                        oa3.start()

                    }


                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super .onCreateOptionsMenu(menu, inflater)
    }

}

