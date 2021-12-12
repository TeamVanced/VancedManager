package com.vanced.manager.core.installer.util

enum class PMRootStatusType {
    ACTION_FAILED_SET_INSTALLER,
    ACTION_FAILED_GET_PACKAGE_DIR,
    ACTION_FAILED_FORCE_STOP_APP,

    INSTALL_SUCCESSFUL,
    INSTALL_FAILED_ABORTED,
    INSTALL_FAILED_ALREADY_EXISTS,
    INSTALL_FAILED_CPU_ABI_INCOMPATIBLE,
    INSTALL_FAILED_INSUFFICIENT_STORAGE,
    INSTALL_FAILED_INVALID_APK,
    INSTALL_FAILED_VERSION_DOWNGRADE,
    INSTALL_FAILED_PARSE_NO_CERTIFICATES,
    INSTALL_FAILED_UNKNOWN,

    LINK_FAILED_UNMOUNT,
    LINK_FAILED_MOUNT,

    PATCH_FAILED_COPY,
    PATCH_FAILED_CHMOD,
    PATCH_FAILED_CHOWN,
    PATCH_FAILED_CHCON,
    PATCH_FAILED_DESTROY,

    SESSION_FAILED_CREATE,
    SESSION_FAILED_WRITE,
    SESSION_FAILED_COPY,
    SESSION_INVALID_ID,

    SCRIPT_FAILED_SETUP_POST_FS,
    SCRIPT_FAILED_SETUP_SERVICE_D,
    SCRIPT_FAILED_DESTROY_POST_FS,
    SCRIPT_FAILED_DESTROY_SERVICE_D,

    UNINSTALL_SUCCESSFUL,
    UNINSTALL_FAILED,
}

sealed class PMRootStatus<out V> {
    data class Success<out V>(val value: V? = null) : PMRootStatus<V>()
    data class Error(val error: PMRootStatusType, val message: String) : PMRootStatus<Nothing>()
}

inline fun <V, S : PMRootStatus<V>> S.exec(
    onError: (PMRootStatusType, String) -> Unit,
    onSuccess: () -> Unit
) {
    when (this) {
        is PMRootStatus.Success<*> -> {
            onSuccess()
        }
        is PMRootStatus.Error -> {
            onError(error, message)
        }
    }
}

inline fun <reified V, S : PMRootStatus<V>> S.execWithValue(
    onError: (PMRootStatusType, String) -> Unit,
    onSuccess: (value: V?) -> Unit
) {
    when (this) {
        is PMRootStatus.Success<*> -> {
            onSuccess(value as V?)
        }
        is PMRootStatus.Error -> {
            onError(error, message)
        }
    }
}