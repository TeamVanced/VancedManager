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

class LinkAdapter(
    private val context: Context,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {

    private val instagram = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_instagram),
        INSTAGRAM
    )

    private val youtube = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_youtube),
        YOUTUBE
    )

    private val github = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_github),
        GITHUB
    )

    private val website = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_website),
        WEBSITE
    )

    private val telegram = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_telegram),
        TELEGRAM
    )

    private val twitter = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_twitter),
        TWITTER
    )

    private val discord = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_discord),
        DISCORD
    )

    private val reddit = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_reddit),
        REDDIT
    )

    val links = arrayOf(instagram, youtube, github, website, telegram, twitter, discord, reddit)

    inner class LinkViewHolder(private val binding: ViewSocialLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val logo = binding.linkImage

        fun bind(position: Int) {
            binding.linkBg.setOnClickListener {
                viewModel.openUrl(context, links[position].linkUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = ViewSocialLinkBinding.inflate(LayoutInflater.from(context), parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(position)
        holder.logo.setImageDrawable(links[position].linkIcon)
    }

    override fun getItemCount(): Int = links.size

    companion object {
        const val INSTAGRAM = "https://instagram.com/vanced.youtube"
        const val YOUTUBE = "https://youtube.com/c/YouTubeVanced"
        const val GITHUB = "https://github.com/YTVanced/VancedManager"
        const val WEBSITE = "https://vancedapp.com"
        const val TELEGRAM = "https://t.me/joinchat/AAAAAEHf-pi4jH1SDlAL4w"
        const val TWITTER = "https://twitter.com/YTVanced"
        const val DISCORD = "https://discord.gg/WCGNdRruzb"
        const val REDDIT = "https://www.reddit.com/r/Vanced/"
    }

}