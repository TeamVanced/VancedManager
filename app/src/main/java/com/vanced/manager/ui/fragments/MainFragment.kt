package com.vanced.manager.ui.fragments

import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.title_home)
        setHasOptionsMenu(true)
        return inflater.inflate(inflater, R.layout.fragment_main, container, false)
    }
    
}
