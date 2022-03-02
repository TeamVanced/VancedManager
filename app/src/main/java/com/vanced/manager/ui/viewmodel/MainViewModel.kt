package com.vanced.manager.ui.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vanced.manager.domain.model.App
import com.vanced.manager.installer.util.PM
import com.vanced.manager.repository.AppRepository
import com.vanced.manager.repository.ManagerMode
import com.vanced.manager.repository.PreferenceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException

class MainViewModel(
    private val appRepository: AppRepository,
    private val preferenceRepository: PreferenceRepository,
    private val app: Application,
) : AndroidViewModel(app) {

    var appMode by mutableStateOf(preferenceRepository.managerMode)
    var appTheme by mutableStateOf(preferenceRepository.managerTheme)

    private val appCount
        get() = when (appMode) {
            ManagerMode.ROOT -> 2
            ManagerMode.NONROOT -> 3
        }

    var appState by mutableStateOf<ManagerState>(ManagerState.Fetching(appCount))
        private set

    fun fetch() {
        viewModelScope.launch {
            try {
                supervisorScope {
                    appState = ManagerState.Fetching(appCount)

                    when (appMode) {
                        ManagerMode.ROOT -> {
                            appState = ManagerState.Success(
                                apps = listOf(
                                    async { appRepository.getVancedYoutubeRoot() },
                                    async { appRepository.getVancedYoutubeMusicRoot() }
                                ).awaitAll()
                            )
                        }
                        ManagerMode.NONROOT -> {
                            appState = ManagerState.Success(
                                apps = listOf(
                                    async { appRepository.getVancedYoutubeNonroot() },
                                    async { appRepository.getVancedYoutubeMusicNonroot() },
                                    async { appRepository.getVancedMicrog() }
                                ).awaitAll()
                            )
                        }
                    }
                }
            } catch (e: HttpException) {
                appState = ManagerState.Error(e.message())
            } catch (e: Exception) {
                appState = ManagerState.Error(e.toString())
            }
        }
    }

    fun launchApp(
        packageName: String,
        launchActivity: String
    ) {
        val component = ComponentName(packageName, launchActivity)
        val intent = Intent().apply {
            setComponent(component)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        app.startActivity(intent)
    }

    //TODO implement root uninstallation
    fun uninstallApp(
        appPackage: String,
    ) {
        PM.uninstallPackage(appPackage, app)
    }

}

sealed class ManagerState {
    data class Fetching(val placeholderAppsCount: Int) : ManagerState()
    data class Success(val apps: List<App>) : ManagerState()
    data class Error(val error: String) : ManagerState()

    val isFetching get() = this is Fetching
    val isSuccess get() = this is Success
    val isError get() = this is Error
}