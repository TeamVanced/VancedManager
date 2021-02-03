package com.vanced.manager.core.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.vanced.manager.utils.AppUtils.log

class CloudMessaging : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        log("VMC", "Generated new token: $p0")
    }

}