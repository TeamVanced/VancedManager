package com.vanced.manager.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

fun CoroutineContext.asExecutor(): Executor = object : Executor {
    private val scope = CoroutineScope(this@asExecutor)

    override fun execute(command: Runnable) {
        scope.launch { command.run() }
    }
}