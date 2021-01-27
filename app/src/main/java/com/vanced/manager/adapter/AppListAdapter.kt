package com.vanced.manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.viewtooltip.ViewTooltip
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewAppBinding
import com.vanced.manager.model.DataModel
import com.vanced.manager.model.RootDataModel
import com.vanced.manager.ui.dialogs.AppInfoDialog
import com.vanced.manager.ui.viewmodels.HomeViewModel
import com.vanced.manager.utils.enableMusic
import com.vanced.manager.utils.enableVanced
import com.vanced.manager.utils.managerVariant

class AppListAdapter(
    private val context: FragmentActivity,
    private val viewModel: HomeViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val tooltip: ViewTooltip
) : RecyclerView.Adapter<AppListAdapter.ListViewHolder>() {

    val apps = mutableListOf<String>()
    private val dataModels = mutableListOf<DataModel?>()
    private val rootDataModels = mutableListOf<RootDataModel?>()
    private val prefs = getDefaultSharedPreferences(context)
    private var itemCount = 0

    private val isRoot = prefs.managerVariant == "root"

    inner class ListViewHolder(private val binding: ViewAppBinding) : RecyclerView.ViewHolder(binding.root) {
        val appCard = binding.appCard
        fun bind(position: Int) {
            val dataModel = if (isRoot) rootDataModels[position] else dataModels[position]
            with(binding) {
                appName.text = dataModel?.appName
                dataModel?.buttonTxt?.observe(lifecycleOwner) {
                    appInstallButton.text = it
                }
                appInstallButton.setOnClickListener {
                    viewModel.openInstallDialog(it, apps[position])
                }
                appUninstall.setOnClickListener {
                    dataModel?.appPkg?.let { it1 -> viewModel.uninstallPackage(it1) }
                }
                appLaunch.setOnClickListener {
                    viewModel.launchApp(apps[position], isRoot)
                }
                with(dataModel?.isAppInstalled?.value) {
                    appUninstall.isVisible = this == true
                    appLaunch.isVisible = this == true
                }
                dataModel?.isAppInstalled?.observe(lifecycleOwner) {
                    appUninstall.isVisible = it
                    appLaunch.isVisible = it
                }
                dataModel?.versionName?.observe(lifecycleOwner) {
                    appRemoteVersion.text = it
                }
                dataModel?.installedVersionName?.observe(lifecycleOwner) {
                    appInstalledVersion.text = it
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ViewAppBinding.inflate(LayoutInflater.from(context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)
        val dataModel = if (isRoot) rootDataModels[position] else dataModels[position]
        holder.appCard.setOnClickListener {
            tooltip.close()
            AppInfoDialog.newInstance(
                appName = apps[position],
                appIcon = dataModel?.appIcon,
                changelog = dataModel?.changelog?.value
            ).show(context.supportFragmentManager, "info")
        }
    }

    override fun getItemCount(): Int = itemCount

    init {

        if (prefs.enableVanced) {
            if (isRoot) {
                rootDataModels.add(viewModel.vancedRootModel.value)
            } else {
                dataModels.add(viewModel.vancedModel.value)
            }
            apps.add(context.getString(R.string.vanced))
            itemCount++
        }

        if (prefs.enableMusic) {
            if (isRoot) {
                rootDataModels.add(viewModel.musicRootModel.value)
            } else {
                dataModels.add(viewModel.musicModel.value)
            }
            apps.add(context.getString(R.string.music))
            itemCount++
        }

        if (!isRoot) {
            dataModels.add(viewModel.microgModel.value)
            apps.add(context.getString(R.string.microg))
            itemCount++
        }

    }


}