package com.vanced.manager.core.util

sealed class Message {
    data class Success(val message: String) : Message()
    data class Warning(val message: String) : Message()
    data class Error(val message: String) : Message()
}

val downloadLogs = mutableListOf<Message>()