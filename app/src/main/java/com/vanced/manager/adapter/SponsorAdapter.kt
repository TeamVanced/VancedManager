package com.vanced.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewSponsorBinding
import com.vanced.manager.model.SponsorModel
import com.vanced.manager.ui.viewmodels.HomeViewModel

class SponsorAdapter(
    private val context: Context,
    private val viewModel: HomeViewModel,
    //private val json: ObservableField<JsonObject?>
) : RecyclerView.Adapter<SponsorAdapter.LinkViewHolder>() {

    private val brave = SponsorModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_brave),
        "Brave",
        BRAVE
    )

    private val adguard = SponsorModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_adguard),
        "AdGuard",
        ADGUARD
    )

    val sponsors = arrayListOf(brave, adguard)

    inner class LinkViewHolder(private val binding: ViewSponsorBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        val logo = binding.sponsorLogo
        fun bind(position: Int) {
            with(binding) {
                sponsorName.text = sponsors[position].name
                cardSponsor.setOnClickListener {
                    viewModel.openUrl(sponsors[position].url)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = ViewSponsorBinding.inflate(LayoutInflater.from(context), parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(position)
        holder.logo.setImageDrawable(sponsors[position].logo)
    }

    override fun getItemCount(): Int = 2

    companion object {
        const val BRAVE = "https://vancedapp.com/brave"
        const val ADGUARD = "https://adguard.com/?aid=31141&source=manager"
    }

}