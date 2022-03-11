package com.vanced.manager.util

//Dear reader, welcome to HELL.
//We don't kink-shame here.

inline fun <reified E1 : Exception, reified E2 : Exception, R> doubleUnionTryCatch(
    onCatch: (Exception) -> R,
    onTry: () -> R
): R {
    return try {
        onTry()
    } catch (e: Exception) {
        when (e) {
            is E1, is E2 -> onCatch(e)
            else -> throw e
        }
    }
}

inline fun <reified E1 : Exception, reified E2 : Exception, reified E3 : Exception, R> tripleUnionTryCatch(
    onCatch: (Exception) -> R,
    onTry: () -> R
): R {
    return try {
        onTry()
    } catch (e: Exception) {
        when (e) {
            is E1, is E2, is E3 -> onCatch(e)
            else -> throw e
        }
    }
}