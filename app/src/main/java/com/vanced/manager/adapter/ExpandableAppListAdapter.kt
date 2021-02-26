package com.vanced.manager.adapter

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewAppExpandableBinding
import com.vanced.manager.model.DataModel
import com.vanced.manager.ui.dialogs.AppInfoDialog
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

    inner class ListViewHolder(private val binding: ViewAppExpandableBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isExpanded = false
        private var isAnimationRunning = false

        private fun Animator.addAnimListener() {
            addListener(
                onStart = {
                    isAnimationRunning = true
                },
                onEnd = {
                    isAnimationRunning = false
                }
            )
        }

        fun bind(position: Int) {
            val dataModel = dataModels[position]
            with(binding) {
                appTitle.text = dataModel?.appName
                appDescription.text = dataModel?.appDescription
                dataModel?.appIcon?.let { appIcon.setImageResource(it) }
                appDownload.setOnClickListener {
                    viewModel.openInstallDialog(dataModel?.buttonTag?.value, apps[position])
                }
                appExpandCard.setOnClickListener {
                    if (isAnimationRunning) return@setOnClickListener
                    val rootHeight = root.measuredHeight
                    val expandedViewHeight = appExpandedView.height
                    val expandedTranslation = appExpandCard.height.toFloat()

                    when (isExpanded.also { isExpanded = !isExpanded }) {
                        true -> {
                            appExpandedView.toggle(0f, 0.8f, -expandedTranslation)
                            root.toggleCard(rootHeight - expandedViewHeight) { addAnimListener() }
                            appExpandArrow.rotateArrow(90f)
                            appExpandCard.animateCardRadius(0f, 16f)
                        }
                        false -> {
                            root.toggleCard(rootHeight + expandedViewHeight) { addAnimListener() }
                            appExpandedView.toggle(1f, 1f, expandedTranslation)
                            appExpandArrow.rotateArrow(-90f)
                            appExpandCard.animateCardRadius(16f, 0f)
                        }
                    }
                }
                appUninstall.setOnClickListener {
                    dataModel?.appPkg?.let { it1 -> viewModel.uninstallPackage(it1) }
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
                dataModel?.buttonImage?.observe(activity) {
                    if (it != null) {
                        appDownload.setImageDrawable(it)
                    }
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

    private fun MaterialCardView.animateCardRadius(startPoint: Float, endPoint: Float) {
        ValueAnimator.ofFloat(startPoint, endPoint).setDuration(animationDuration).apply {
            addUpdateListener {
                radius = it.animatedValue as Float
            }
        }.start()
    }

    private inline fun MaterialCardView.toggleCard(
        resultHeight: Int,
        onAnimation: Animator.() -> Unit
    ) {
        ValueAnimator.ofInt(measuredHeight, resultHeight).apply {
            duration = animationDuration
            addUpdateListener { value ->
                layoutParams = layoutParams.apply { height = value.animatedValue as Int }
            }
            onAnimation()
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