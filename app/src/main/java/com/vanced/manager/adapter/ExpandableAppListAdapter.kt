package com.vanced.manager.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewAppExpandableBinding
import com.vanced.manager.model.ButtonTag
import com.vanced.manager.model.DataModel
import com.vanced.manager.ui.dialogs.AppInfoDialog
import com.vanced.manager.ui.dialogs.AppUninstallDialog
import com.vanced.manager.ui.viewmodels.HomeViewModel
import com.vanced.manager.utils.*

class ExpandableAppListAdapter(
    private val activity: FragmentActivity,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<ExpandableAppListAdapter.ListViewHolder>() {

    private val apps = mutableListOf<String>()
    private val dataModels = mutableListOf<DataModel?>()
    private val prefs = getDefaultSharedPreferences(activity)

    private val isRoot = prefs.managerVariant == "root"

    private var isAnimationRunning = false

    inner class ListViewHolder(private val binding: ViewAppExpandableBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isExpanded = false

        fun bind(position: Int) {
            val dataModel = dataModels[position]
            with(binding) {
                appTitle.text = dataModel?.appName
                appDescription.text = dataModel?.appDescription
                dataModel?.appIcon?.let { appIcon.setImageResource(it) }
                appClickableLayout.setOnClickListener {
                    if (isAnimationRunning) return@setOnClickListener
                    val rootHeight = root.measuredHeight
                    val expandedViewHeight = appExpandedView.height
                    val expandedTranslation = appClickableLayout.height.toFloat()

                    when (isExpanded.also { isExpanded = !isExpanded }) {
                        true -> {
                            appExpandedView.toggle(0f, 0.8f, -expandedTranslation)
                            root.toggleCard(rootHeight - expandedViewHeight)
                            appExpandArrow.rotateArrow(90f)
                        }
                        false -> {
                            root.toggleCard(rootHeight + expandedViewHeight)
                            appExpandedView.toggle(1f, 1f, expandedTranslation)
                            appExpandArrow.rotateArrow(-90f)
                        }
                    }
                }
                appUninstall.setOnClickListener {
                    AppUninstallDialog.newInstance(
                        dataModel?.appName,
                        dataModel?.appPkg
                    ).show(activity.supportFragmentManager, null)
                }
                appLaunch.setOnClickListener {
                    viewModel.launchApp(apps[position], isRoot)
                }
                appInfo.setOnClickListener {
                    AppInfoDialog.newInstance(
                        appName = apps[position],
                        appIcon = dataModel?.appIcon,
                        changelog = dataModel?.changelog?.value
                    ).show(activity.supportFragmentManager, "info")
                }
                dataModel?.buttonTag?.observe(activity) { buttonTag ->
                    appDownload.apply {
                        setOnClickListener {
                            viewModel.openInstallDialog(
                                activity.supportFragmentManager,
                                buttonTag,
                                apps[position]
                            )
                        }
                        appDownload.setIconResource(buttonTag.image)
                        contentDescription = activity.getString(
                            when (buttonTag) {
                                ButtonTag.UPDATE -> R.string.accessibility_update
                                ButtonTag.REINSTALL -> R.string.accessibility_reinstall
                                else -> R.string.accessibility_download
                            }
                        )
                    }
                }
                dataModel?.isAppInstalled?.observe(activity) {
                    appUninstall.isVisible = it
                    appLaunch.isVisible = it
                }
                dataModel?.versionName?.observe(activity) {
                    appVersionLatest.text = it
                    appDownload.isGone = it == activity.getString(R.string.unavailable)
                }
                dataModel?.installedVersionName?.observe(activity) {
                    appVersionInstalled.text = it
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ViewAppExpandableBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = apps.size

    private fun ImageView.rotateArrow(degrees: Float) {
        animate().apply {
            duration = animationDuration
            rotation(degrees)
        }
    }

    private fun View.toggle(
        alpha: Float,
        scale: Float,
        translation: Float
    ) {
        animate().apply {
            duration = animationDuration
            scaleX(scale)
            scaleY(scale)
            alpha(alpha)
            translationYBy(translation)
        }
    }

    private fun MaterialCardView.toggleCard(resultHeight: Int) {
        ValueAnimator.ofInt(measuredHeight, resultHeight).apply {
            duration = animationDuration
            addUpdateListener { value ->
                updateLayoutParams {
                    height = value.animatedValue as Int
                }
            }
            addListener(
                onStart = {
                    isAnimationRunning = true
                },
                onEnd = {
                    isAnimationRunning = false
                }
            )
        }.start()
    }

    init {

        if (prefs.enableVanced) {
            if (isRoot) {
                dataModels.add(viewModel.vancedRootModel.value)
            } else {
                dataModels.add(viewModel.vancedModel.value)
            }
            apps.add(activity.getString(R.string.vanced))
        }

        if (prefs.enableMusic) {
            if (isRoot) {
                dataModels.add(viewModel.musicRootModel.value)
            } else {
                dataModels.add(viewModel.musicModel.value)
            }
            apps.add(activity.getString(R.string.music))
        }

        if (!isRoot) {
            dataModels.add(viewModel.microgModel.value)
            apps.add(activity.getString(R.string.microg))
        }

    }

    companion object {
        const val animationDuration = 250L
    }

}