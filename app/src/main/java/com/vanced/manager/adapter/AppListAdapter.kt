package com.vanced.manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.viewtooltip.ViewTooltip
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewAppBinding
import com.vanced.manager.model.DataModel
import com.vanced.manager.ui.dialogs.AppInfoDialog
import com.vanced.manager.ui.viewmodels.HomeViewModel

class AppListAdapter(
    private val context: FragmentActivity,
    private val viewModel: HomeViewModel,
    private val tooltip: ViewTooltip
) : RecyclerView.Adapter<AppListAdapter.ListViewHolder>() {

    val apps = mutableListOf<String>()
    private val dataModels = mutableListOf<DataModel?>()
    private val rootDataModels = mutableListOf<DataModel?>()
    private val prefs = getDefaultSharedPreferences(context)
    private var itemCount = 0

    private val isRoot = prefs.getString("vanced_variant", "nonroot") == "root"

    inner class ListViewHolder(private val binding: ViewAppBinding) : RecyclerView.ViewHolder(binding.root) {
        val appCard = binding.appCard

        fun bind(position: Int) {
            binding.viewModel = viewModel
            binding.dataModel = if (isRoot) rootDataModels[position] else dataModels[position]
            binding.app = apps[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ViewAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)

        holder.appCard.setOnClickListener {
            tooltip.close()
            AppInfoDialog(
                apps[position],
                dataModels[position]?.appIcon,
                dataModels[position]?.changelog?.get()
            ).show(context.supportFragmentManager, "info")
        }
    }

    override fun getItemCount(): Int = itemCount

    init {

        if (prefs.getBoolean("enable_vanced", true)) {
            dataModels.add(viewModel.vanced.get())
            rootDataModels.add(viewModel.vancedRoot.get())
            apps.add(context.getString(R.string.vanced))
            itemCount++
        }

        if (prefs.getBoolean("enable_music", false)) {
            dataModels.add(viewModel.music.get())
            rootDataModels.add(viewModel.musicRoot.get())
            apps.add(context.getString(R.string.music))
            itemCount++
        }

        if (!isRoot) {
            dataModels.add(viewModel.microg.get())
            apps.add(context.getString(R.string.microg))
            itemCount++
        }

    }


}