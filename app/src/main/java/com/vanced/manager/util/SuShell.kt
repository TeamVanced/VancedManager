package com.vanced.manager.util

import com.topjohnwu.superuser.Shell
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val Shell.Result.outString
    get() = out.joinToString("\n")

val Shell.Result.errString
    get() = err.joinToString("\n")

val isMagiskInstalled
    get() = Shell.rootAccess() && Shell.su("magisk", "-c").exec().isSuccess

suspend fun Shell.Job.await(): Shell.Result {
    return suspendCoroutine { continuation ->
        submit {
            continuation.resume(it)
        }
    }
}

class SuException(val stderrOut: String) : Exception(stderrOut)

@Throws(SuException::class)
suspend fun Shell.Job.awaitOutputOrThrow(): String {
    return suspendCoroutine { continuation ->
        submit {
            if (it.isSuccess) {
                continuation.resume(it.outString)
            } else {
                continuation.resumeWithException(SuException(it.errString))
            }
        }
    }
}

