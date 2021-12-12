package com.vanced.manager.core.util

import com.topjohnwu.superuser.Shell

val Shell.Result.outString
    get() = out.joinToString("\n")

val Shell.Result.errString
    get() = err.joinToString("\n")

val isMagiskInstalled
    get() = Shell.su("magisk", "-c").exec().isSuccess

