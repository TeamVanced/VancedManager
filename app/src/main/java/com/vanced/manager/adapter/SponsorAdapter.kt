package com.vanced.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewSponsorBinding
import com.vanced.manager.model.SponsorModel
import com.vanced.manager.ui.viewmodels.HomeViewModel

class SponsorAdapter(context: Context, private val viewModel: HomeViewModel) : RecyclerView.Adapter<SponsorAdapter.LinkViewHolder>() {

    private val brave = SponsorModel(
        ContextCompat.getDrawable(context, R.drawable.ic_brave),
        "Brave",
        "https://vancedapp.com/brave"
    )

    private val adguard = SponsorModel(
        ContextCompat.getDrawable(context, R.drawable.ic_adguard),
        "AdGuard",
        "https://vancedapp.com/adguard"
    )

    val sponsors = arrayOf(brave, adguard)

    inner class LinkViewHolder(private val binding: ViewSponsorBinding) : RecyclerView.ViewHolder(binding.root) {
        //val cardBg = binding.linkBg
        //val descText = binding.linkAppDescription
        fun bind(position: Int) {
            binding.viewModel = viewModel
            binding.sponsor = sponsors[position]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = ViewSponsorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        //holder.cardBg.setCardBackgroundColor()
        holder.bind(position)
//        val lp = holder.cardBg
//        if (lp is FlexboxLayoutManager.LayoutParams) {
//            val flexboxLp = lp as FlexboxLayoutManager.LayoutParams
//            flexboxLp.flexGrow = 1.0f
//            flexboxLp.alignSelf = AlignItems.STRETCH
//        }
    }

    override fun getItemCount(): Int = 2

}