package com.vanced.manager.network.model

import android.content.Context
import com.vanced.manager.R
import com.vanced.manager.core.preferences.holder.musicVersionPref
import com.vanced.manager.core.preferences.holder.vancedLanguagesPref
import com.vanced.manager.core.preferences.holder.vancedThemePref
import com.vanced.manager.core.preferences.holder.vancedVersionPref
import com.vanced.manager.domain.datasource.PackageInformationDataSource
import com.vanced.manager.domain.model.App
import com.vanced.manager.domain.model.AppStatus
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.domain.model.InstallationOptionItem
import com.vanced.manager.domain.util.EntityMapper
import com.vanced.manager.network.util.MUSIC_NAME
import com.vanced.manager.network.util.VANCED_NAME
import java.util.*

class AppDtoMapper(
    private val packageInformationDataSource: PackageInformationDataSource,
    context: Context
) : EntityMapper<AppDto, App> {

    private val latestVersionRadioButton =
        InstallationOptionItem(
            displayText = { context.getString(R.string.app_version_dialog_option_latest) },
            key = "latest"
        )

    override suspend fun mapToModel(entity: AppDto): App =
        with(entity) {
            val localVersionCode = packageInformationDataSource.getVersionCode(packageName)
            val localVersionCodeRoot =
                packageInformationDataSource.getVersionCode(packageNameRoot ?: "")
            val localVersionName = packageInformationDataSource.getVersionName(packageName)
            val localVersionNameRoot =
                packageInformationDataSource.getVersionName(packageNameRoot ?: "")
            App(
                name = name,
                remoteVersion = version,
                remoteVersionCode = versionCode,
                installedVersion = localVersionName,
                installedVersionCode = localVersionCode,
                installedVersionRoot = localVersionNameRoot,
                installedVersionCodeRoot = localVersionCodeRoot,
                appStatus = compareVersionCodes(versionCode, localVersionCode),
                appStatusRoot = compareVersionCodes(versionCode, localVersionCodeRoot),
                packageName = packageName,
                packageNameRoot = packageNameRoot,
                iconUrl = iconUrl,
                changelog = changelog,
                url = url,
                versions = versions,
                themes = themes,
                languages = languages,
                installationOptions = getInstallationOptions(name, themes, versions, languages)
            )
        }

    private fun compareVersionCodes(remote: Int?, local: Int?) =
        if (local != null && remote != null) {
            when {
                remote > local -> AppStatus.Update
                remote <= local -> AppStatus.Reinstall
                else -> AppStatus.Install
            }
        } else {
            AppStatus.Install
        }

    private fun getInstallationOptions(
        appName: String,
        appThemes: List<String>?,
        appVersions: List<String>?,
        appLanguages: List<String>?,
    ) = when (appName) {
            VANCED_NAME -> listOf(
                InstallationOption.SingleSelect(
                    titleId = R.string.app_installation_options_theme,
                    getOption = { vancedThemePref },
                    setOption = {
                        vancedThemePref = it
                    },
                    items = appThemes?.map { theme ->
                        InstallationOptionItem(
                            displayText = {
                                theme.replaceFirstChar {
                                    it.titlecase(Locale.getDefault())
                                }
                            },
                            key = theme
                        )
                    } ?: emptyList(),
                ),
                InstallationOption.SingleSelect(
                    titleId = R.string.app_installation_options_version,
                    getOption = { vancedVersionPref },
                    setOption = {
                        vancedVersionPref = it
                    },
                    items = appVersions?.map { version ->
                        InstallationOptionItem(
                            displayText = { "v$version" },
                            key = version
                        )
                    }?.plus(latestVersionRadioButton)?.reversed() ?: emptyList(),
                ),
                InstallationOption.MultiSelect(
                    titleId = R.string.app_installation_options_language,
                    getOption = { vancedLanguagesPref },
                    addOption = {
                        vancedLanguagesPref = vancedLanguagesPref + it
                    },
                    removeOption = {
                        vancedLanguagesPref = vancedLanguagesPref - it
                    },
                    items = appLanguages?.map { language ->
                        InstallationOptionItem(
                            displayText = {
                                val locale = Locale(it)
                                locale.getDisplayName(locale)
                            },
                            key = language
                        )
                    } ?: emptyList(),
                ),
            )
            MUSIC_NAME -> listOf(
                InstallationOption.SingleSelect(
                    titleId = R.string.app_installation_options_version,
                    getOption = { musicVersionPref },
                    setOption = {
                        musicVersionPref = it
                    },
                    items = appVersions?.map { version ->
                        InstallationOptionItem(
                            displayText = { version },
                            key = version
                        )
                    } ?: emptyList(),
                )
            )
            else -> null
        }
}