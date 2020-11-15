package com.vanced.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewSocialLinkBinding
import com.vanced.manager.model.LinkModel
import com.vanced.manager.ui.viewmodels.HomeViewModel

class LinkAdapter(context: Context, private val viewModel: HomeViewModel) : RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {

    private val instagram = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_instagram),
        "https://instagram.com/vanced.youtube"
    )

    private val youtube = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_youtube),
        "https://youtube.com/c/YouTubeVanced"
    )

    private val github = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_github),
        "https://github.com/YTVanced/VancedManager"
    )

    private val website = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_website),
        "https://vancedapp.com"
    )

    private val telegram = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_telegram),
        "https://t.me/joinchat/AAAAAEHf-pi4jH1SDlAL4w"
    )

    private val twitter = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_twitter),
        "https://twitter.com/YTVanced"
    )

    private val discord = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_discord),
        "https://discord.gg/WCGNdRruzb"
    )

    private val reddit = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_reddit),
        "https://www.reddit.com/r/Vanced/"
    )

    val links = arrayOf(instagram, youtube, github, website, telegram, twitter, discord, reddit)

    inner class LinkViewHolder(private val binding: ViewSocialLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        val logo = binding.linkImage
        fun bind(position: Int) {
            binding.viewModel = viewModel
            binding.linkModel = links[position]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = ViewSocialLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(position)
        holder.logo.setImageDrawable(links[position].linkIcon)
    }

    override fun getItemCount(): Int = links.size

}