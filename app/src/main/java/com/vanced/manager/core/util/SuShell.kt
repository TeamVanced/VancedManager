package com.vanced.manager.core.util

import com.topjohnwu.superuser.Shell
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val Shell.Result.outString
    get() = out.joinToString("\n")

val Shell.Result.errString
    get() = err.joinToString("\n")

val isMagiskInstalled
    get() = Shell.rootAccess() && Shell.su("magisk", "-c").exec().isSuccess

suspend fun Shell.Job.await() =
    suspendCoroutine<Shell.Result> { continuation ->
        submit(/*continuation.context.asExecutor(),*/ continuation::resume)
    }

