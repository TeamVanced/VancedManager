package com.vanced.manager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanced.manager.R
import com.vanced.manager.ui.viewmodels.HomeViewModel

class ChangelogAdapter(private val variant: String, viewModel: HomeViewModel?): RecyclerView.Adapter<ChangelogAdapter.ChangelogViewHolder>() {

    private val nonrootChangelogs = arrayOf(viewModel?.vanced?.get()?.changelog?.get(), viewModel?.music?.get()?.changelog?.get(), viewModel?.microg?.get()?.changelog?.get(), viewModel?.manager?.get()?.changelog?.get())
    private val rootChangelogs = arrayOf(viewModel?.vanced?.get()?.changelog?.get(), viewModel?.manager?.get()?.changelog?.get())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangelogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_changelog, parent, false)

        return ChangelogViewHolder(view)
    }

    override fun getItemCount(): Int = if (variant == "root") 2 else 4

    override fun onBindViewHolder(holder: ChangelogViewHolder, position: Int) {
        holder.changelog.text =
            if (variant == "root")
                rootChangelogs[position]
            else
                nonrootChangelogs[position]

    }

    open class ChangelogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val changelog: TextView = itemView.findViewById(R.id.changelog_text)
    }

}
