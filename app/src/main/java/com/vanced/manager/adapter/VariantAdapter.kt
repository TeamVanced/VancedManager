package com.vanced.manager.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewHomeBinding
import com.vanced.manager.ui.viewmodels.HomeViewModel

class VariantAdapter(private val viewModel: HomeViewModel, private val context: Context) : RecyclerView.Adapter<VariantAdapter.VariantAdapterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantAdapterHolder {
        val view = ViewHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VariantAdapterHolder(view, context)
    }

    override fun onBindViewHolder(holder: VariantAdapterHolder, position: Int) {
        val variants = arrayOf("nonroot", "root")
        holder.bind(variants[position], viewModel)
    }

    override fun getItemCount(): Int = 2

    class VariantAdapterHolder(private val homeBinding: ViewHomeBinding, private val context: Context) : RecyclerView.ViewHolder(homeBinding.root) {
        private var isExpanded: Boolean = false

        fun bind(variant: String, viewModel: HomeViewModel) {
            with (homeBinding) {
                homeBinding.variant = variant
                homeBinding.viewModel = viewModel

                with(includeChangelogsLayout) {
                    changelogButton.setOnClickListener {
                        with (includeChangelogsLayout) {
                            viewpager.visibility = if (isExpanded) View.GONE else View.VISIBLE
                            tablayout.visibility = if (isExpanded) View.GONE else View.VISIBLE
                            changelogButton.animate().apply {
                                rotation(if (isExpanded) 0F else 180F)
                                interpolator = AccelerateDecelerateInterpolator()
                            }
                            isExpanded = !isExpanded
                        }
                    }

                    viewpager.adapter = ChangelogAdapter(variant, viewModel)
                    val nonrootTitles = arrayOf("Vanced", "Music", "microG", "Manager")
                    val rootTitles = arrayOf("Vanced", "Manager")

                    TabLayoutMediator(tablayout, viewpager) { tab, position ->
                        tab.text =
                            if (variant == "root") {
                                rootTitles[position]
                            } else {
                                nonrootTitles[position]
                            }
                    }.attach()
                }

                includeVancedLayout.vancedCard.setOnLongClickListener {
                    versionToast("Vanced", viewModel.vanced.get()?.installedVersionName?.get()!!)
                    true
                }

                includeMusicLayout.musicCard.setOnLongClickListener {
                    versionToast("Music", viewModel.music.get()?.installedVersionName?.get()!!)
                    true
                }

                includeMicrogLayout.microgCard.setOnLongClickListener {
                    versionToast("MicroG", viewModel.microg.get()?.installedVersionName?.get()!!)
                    true
                }
            }
        }
        private fun versionToast(name: String, app: String?) {
            val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.setPrimaryClip(ClipData.newPlainText(name, app))
            Toast.makeText(context, context.getString(R.string.version_toast, name), Toast.LENGTH_LONG).show()
        }
    }

}