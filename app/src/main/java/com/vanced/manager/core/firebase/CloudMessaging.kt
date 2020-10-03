package com.vanced.manager.core.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class CloudMessaging : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("VMC", "Generated new token: $p0")
    }

}