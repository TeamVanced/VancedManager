package com.vanced.manager.ui.fragments

import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.animation.addListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.viewpager2.widget.ViewPager2
import com.dezlum.codelabs.getjson.GetJson
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vanced.manager.R
import com.vanced.manager.adapter.SectionPageAdapter
import com.vanced.manager.adapter.SectionPageRootAdapter
import com.vanced.manager.core.fragments.Home
import com.vanced.manager.core.installer.MicrogInstaller.installMicrog
import com.vanced.manager.core.installer.RootAppUninstaller
import com.vanced.manager.databinding.FragmentHomeBinding
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.ui.viewmodels.HomeViewModel
import com.vanced.manager.utils.PackageHelper.installApp
import com.vanced.manager.utils.PackageHelper.isPackageInstalled
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream

class HomeFragment : Home() {

    private lateinit var sectionPageAdapter: SectionPageAdapter
    private lateinit var sectionPageRootAdapter: SectionPageRootAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentHomeBinding
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.title_home)
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNetworkFun()
        val viewModel: HomeViewModel by viewModels()
        binding.viewModel = viewModel

        val variantPref = getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")
        //val signatureStatus = getDefaultSharedPreferences(activity).getString("signature_status", "unavailable")
        registerReceivers()

        if (variantPref == "root") {
            attachRootChangelog()
        } else
            attachNonrootChangelog()

    }

    override fun onPause() {
        super.onPause()
        activity?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(broadcastReceiver) }
    }

    private fun initNetworkFun() {
        val pm = activity?.packageManager
        val variant = getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")
        val microgStatus = pm?.let { isPackageInstalled("com.mgoogle.android.gms", it) }
        val vancedStatus =
            if (variant == "root") {
                pm?.let { isPackageInstalled("com.google.android.youtube", it) }
            } else {
                pm?.let { isPackageInstalled("com.vanced.android.youtube", it) }
            }
        val vancedinstallbtn = view?.findViewById<MaterialButton>(R.id.vanced_installbtn)
        val networkErrorLayout = view?.findViewById<MaterialCardView>(R.id.home_network_wrapper)
        val viewModel: HomeViewModel by viewModels()

        disposable = ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isConnectedToInternet ->
                run {
                    if (isConnectedToInternet) {
                        vancedinstallbtn?.visibility = View.VISIBLE

                        val vancedRemoteCode =
                            GetJson().AsJSONObject("https://vanced.app/api/v1/vanced.json")
                                .get("versionCode").asInt
                        val microgRemoteCode =
                            GetJson().AsJSONObject("https://vanced.app/api/v1/microg.json")
                                .get("versionCode").asInt

                        if (variant == "nonroot") {
                            val microginstallbtn =
                                view?.findViewById<MaterialButton>(R.id.microg_installbtn)
                            microginstallbtn?.visibility = View.VISIBLE

                            if (microgStatus!!) {
                                @Suppress("DEPRECATION")
                                val microgVerCode =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                                        pm.getPackageInfo("com.mgoogle.android.gms", 0).longVersionCode.and(0xFFFFFFFF).toInt()
                                    else
                                        pm.getPackageInfo("com.mgoogle.android.gms", 0).versionCode
                                when {
                                    microgRemoteCode > microgVerCode -> {
                                        microginstallbtn?.text =
                                            activity?.getString(R.string.update)
                                        microginstallbtn?.icon =
                                            activity?.getDrawable(R.drawable.ic_cloud_upload_black_24dp)
                                    }

                                    microgRemoteCode == microgVerCode -> {
                                        microginstallbtn?.text =
                                            activity?.getString(R.string.button_installed)
                                        microginstallbtn?.icon =
                                            activity?.getDrawable(R.drawable.outline_cloud_done_24)
                                    }
                                }
                            } else {
                                disableVancedButton(getString(R.string.no_microg))
                            }
                        }

                        if (vancedStatus!!) {
                            val vanPkgName =
                                if (variant == "root")
                                    "com.google.android.youtube"
                                else
                                    "com.vanced.android.youtube"

                            @Suppress("DEPRECATION")
                            val vancedVerCode =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    pm?.getPackageInfo(
                                        vanPkgName,
                                        0
                                    )?.longVersionCode?.and(0xFFFFFFFF)?.toInt()
                                }
                                else {
                                    pm?.getPackageInfo(
                                        vanPkgName,
                                        0
                                    )?.versionCode
                                }

                            when {
                                vancedRemoteCode > vancedVerCode!! -> {
                                    vancedinstallbtn?.text =
                                        activity?.getString(R.string.update)
                                    vancedinstallbtn?.icon =
                                        activity?.getDrawable(R.drawable.ic_cloud_upload_black_24dp)
                                }

                                vancedRemoteCode == vancedVerCode -> {
                                    vancedinstallbtn?.text =
                                        activity?.getString(R.string.button_installed)
                                    vancedinstallbtn?.icon =
                                        activity?.getDrawable(R.drawable.outline_cloud_done_24)
                                }

                            }

                        }

                        if (variant == "root" && viewModel.signatureStatusTxt.value != getString(R.string.signature_disabled)) {
                            when (viewModel.signatureStatusTxt.value) {
                                getString(R.string.unavailable) -> disableVancedButton(getString(R.string.signature_not_checked))
                                getString(R.string.signature_enabled) -> disableVancedButton(getString(R.string.signature_disable))
                                else -> throw NotImplementedError("Error handling status")
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
                        if (variant == "nonroot") {
                            view?.findViewById<MaterialButton>(R.id.microg_installbtn)?.visibility = View.INVISIBLE
                        }

                        vancedinstallbtn?.visibility = View.INVISIBLE

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

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val viewModel: HomeViewModel by viewModels()
            when (intent.action) {
                SIGNATURE_DISABLED -> {
                    activity?.runOnUiThread {
                        viewModel.signatureStatusTxt.value = getString(R.string.signature_disabled)
                    }
                    val mIntent = Intent(activity, RootAppUninstaller::class.java)
                    mIntent.putExtra("Data", "com.vanced.stub")
                    activity?.startService(mIntent)
                    activity?.recreate()
                }
                SIGNATURE_ENABLED -> {
                    activity?.runOnUiThread {
                        viewModel.signatureStatusTxt.value = getString(R.string.signature_enabled)
                    }
                    activity?.recreate()
                }
                MICROG_DOWNLOADING -> {
                    val progress = intent.getIntExtra("microgProgress", 0)
                    val progressbar = view?.findViewById<ProgressBar>(R.id.microg_progress)
                    val downloadTxt = intent.getStringExtra("fileName")
                    val txt = view?.findViewById<TextView>(R.id.microg_downloading)
                    txt?.visibility = View.VISIBLE
                    txt?.text = downloadTxt
                    progressbar?.visibility = View.VISIBLE
                    progressbar?.progress = progress
                }
                VANCED_DOWNLOADING -> {
                    val progress = intent.getIntExtra("vancedProgress", 0)
                    val progressbar = view?.findViewById<ProgressBar>(R.id.vanced_progress)
                    val downloadTxt = intent.getStringExtra("fileName")
                    val txt = view?.findViewById<TextView>(R.id.vanced_downloading)
                    txt?.visibility = View.VISIBLE
                    txt?.text = downloadTxt
                    progressbar?.visibility = View.VISIBLE
                    progressbar?.progress = progress
                }
                MICROG_DOWNLOADED -> {
                    view?.findViewById<TextView>(R.id.microg_downloading)?.visibility = View.GONE
                    view?.findViewById<ProgressBar>(R.id.microg_progress)?.visibility = View.GONE
                    activity?.let { installApp(it, it.filesDir.path + "/microg.apk", "com.mgoogle.android.gms") }
                }
                VANCED_DOWNLOADED -> {
                    view?.findViewById<TextView>(R.id.vanced_downloading)?.visibility = View.GONE
                    view?.findViewById<ProgressBar>(R.id.vanced_progress)?.visibility = View.GONE
                    view?.findViewById<ProgressBar>(R.id.vanced_installing)?.visibility = View.VISIBLE
                }
                DOWNLOAD_ERROR -> {
                    val error = intent.getStringExtra("DownloadError") as String
                    Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
                    Log.d("VMDwnld", error)
                }
            }
        }
    }

    private fun registerReceivers() {
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(
                SIGNATURE_DISABLED
            ))
        }
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(
                SIGNATURE_ENABLED
            )
            )
        }
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(
                VANCED_DOWNLOADING
            )
            )
        }
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(
                MICROG_DOWNLOADING
            )
            )
        }
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(
                VANCED_DOWNLOADED
            )
            )
        }
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(
                MICROG_DOWNLOADED
            )
            )
        }
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(
                DOWNLOAD_ERROR
            )
            )
        }

    }

    private fun attachNonrootChangelog() {
        sectionPageAdapter = SectionPageAdapter(this)
        val tabLayout = view?.findViewById(R.id.tablayout) as TabLayout
        viewPager = view?.findViewById(R.id.viewpager)!!
        viewPager.adapter = sectionPageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Vanced"
                1 -> tab.text = "MicroG"
                2 -> tab.text = "Manager"
            }
        }.attach()
    }

    private fun attachRootChangelog() {
        sectionPageRootAdapter = SectionPageRootAdapter(this)
        val tabLayout = view?.findViewById(R.id.tablayout) as TabLayout
        viewPager = view?.findViewById(R.id.viewpager)!!
        viewPager.adapter = sectionPageRootAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Vanced"
                1 -> tab.text = "Manager"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super .onCreateOptionsMenu(menu, inflater)
    }

    private fun disableVancedButton(txt: String) {
        val vancedinstallbtn = view?.findViewById<MaterialButton>(R.id.vanced_installbtn)
        vancedinstallbtn?.isEnabled = false
        vancedinstallbtn?.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
        vancedinstallbtn?.setTextColor(ColorStateList.valueOf(Color.GRAY))
        vancedinstallbtn?.text = txt
        vancedinstallbtn?.icon = null
    }

    companion object {
        const val SIGNATURE_DISABLED = "Signature verification disabled"
        const val SIGNATURE_ENABLED = "Signature verification enabled"
        const val VANCED_DOWNLOADING = "Vanced downloading"
        const val MICROG_DOWNLOADING = "MicroG downloading"
        const val VANCED_DOWNLOADED = "Vanced downloaded"
        const val MICROG_DOWNLOADED = "MicroG downloaded"
        const val DOWNLOAD_ERROR = "Error occurred"

    }

}

