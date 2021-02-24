package com.vanced.manager.ui.fragments

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.ui.platform.ComposeView
//import androidx.fragment.app.Fragment
//import com.vanced.manager.R
//import com.vanced.manager.ui.compose.Preference
//import com.vanced.manager.ui.compose.PreferenceCategory
//import com.vanced.manager.ui.compose.SwitchPreference
//
//class SettingsFragmentCompose : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return ComposeView(requireActivity()).apply {
//            setContent {
//                LazyColumn {
//                    // use `item` for separate elements like headers
//                    // and `items` for lists of identical elements
//                    item {
//                        PreferenceCategory(
//                            categoryTitle = getString(R.string.category_behaviour)
//                        ) {
//                            SwitchPreference(
//                                preferenceTitle = getString(R.string.use_custom_tabs),
//                                preferenceDescription = getString(R.string.link_custom_tabs),
//                                preferenceKey = "use_custom_tabs"
//                            )
//                        }
//                    }
//                    item {
//                        PreferenceCategory(
//                            categoryTitle = getString(R.string.category_appearance)
//                        ) {
//                            Preference(
//                                preferenceTitle = "test",
//                                preferenceDescription = "test",
//                            ) {}
//                            Preference(
//                                preferenceTitle = "test"
//                            ) {}
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//}