package com.vanced.manager.feature.home.ui.bind

import com.vanced.manager.feature.home.databinding.FragmentHomeBinding
import com.vanced.manager.feature.home.presentation.HomeViewModel
import com.vanced.manager.feature.home.ui.HomeFragment

internal fun HomeFragment.bindData(
    binding: FragmentHomeBinding,
    viewModel: HomeViewModel
) {
    /* requireActivity().title = getString(R.string.title_home)
     setHasOptionsMenu(true)
     with(binding) {
         homeRefresh.setOnRefreshListener { viewModel.fetchData() }
         tooltip = ViewTooltip
             .on(recyclerAppList)
             .position(ViewTooltip.Position.TOP)
             .autoHide(false, 0)
             .color(ResourcesCompat.getColor(requireActivity().resources, R.color.Twitter, null))
             .withShadow(false)
             .corner(25)
             .onHide {
                 prefs.edit { putBoolean("show_changelog_tooltip", false) }
             }
             .text(requireActivity().getString(R.string.app_changelog_tooltip))

         if (prefs.getBoolean("show_changelog_tooltip", true)) {
             tooltip.show()
         }

         recyclerAppList.apply {
             layoutManager = LinearLayoutManager(requireActivity())
             adapter = AppListAdapter(requireActivity(), viewModel, viewLifecycleOwner, tooltip)
             setHasFixedSize(true)
         }

         recyclerSponsors.apply {
             val lm = FlexboxLayoutManager(requireActivity())
             lm.justifyContent = JustifyContent.SPACE_EVENLY
             layoutManager = lm
             setHasFixedSize(true)
             adapter = SponsorAdapter(requireActivity(), viewModel)
         }

         recyclerLinks.apply {
             val lm = FlexboxLayoutManager(requireActivity())
             lm.justifyContent = JustifyContent.SPACE_EVENLY
             layoutManager = lm
             setHasFixedSize(true)
             adapter = LinkAdapter(requireActivity(), viewModel)
         }
     }*/
}