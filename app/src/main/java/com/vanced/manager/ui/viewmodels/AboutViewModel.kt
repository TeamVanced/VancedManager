package com.vanced.manager.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools

class AboutViewModel(application: Application): AndroidViewModel(application) {

    fun openUrl(url: String) {
        InternetTools.openUrl(url, R.color.GitHub, getApplication())
    }

}