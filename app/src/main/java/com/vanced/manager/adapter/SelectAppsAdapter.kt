package com.vanced.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewAppCheckboxBinding
import com.vanced.manager.model.SelectAppModel
import com.vanced.manager.utils.enableMusic
import com.vanced.manager.utils.enableVanced

class SelectAppsAdapter(private val context: Context) :
    RecyclerView.Adapter<SelectAppsAdapter.SelectAppsViewHolder>() {

    private val prefs by lazy { getDefaultSharedPreferences(context) }

    private val vanced = SelectAppModel(
        context.getString(R.string.vanced),
        context.getString(R.string.description_vanced),
        "vanced",
        prefs.enableVanced
    )

    private val music = SelectAppModel(
        context.getString(R.string.music),
        context.getString(R.string.description_vanced_music),
        "music",
        prefs.enableMusic
    )

    val apps = arrayOf(vanced, music)

    inner class SelectAppsViewHolder(binding: ViewAppCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val appName = binding.appCheckboxText
        val appDescription = binding.appCheckboxDescription
        val appCard = binding.appCheckboxBg
        val checkbox = binding.appCheckbox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAppsViewHolder {
        val view = ViewAppCheckboxBinding.inflate(LayoutInflater.from(context), parent, false)
        return SelectAppsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectAppsViewHolder, position: Int) {
        holder.appName.text = apps[position].appName
        holder.appDescription.text = apps[position].appDescription
        holder.checkbox.isChecked = apps[position].isChecked
        holder.appCard.setOnClickListener {
            holder.checkbox.isChecked = !holder.checkbox.isChecked
            apps[position].isChecked = !apps[position].isChecked
        }
    }

    override fun getItemCount(): Int = 2

}