package com.vanced.manager.installer.util

import com.vanced.manager.repository.manager.PackageManagerResult
import com.vanced.manager.repository.manager.getOrElse

object RootPatchHelper {

    fun cleanPatches(app: String): PackageManagerResult<Nothing> {
        val cleanOldPatches = Patcher.destroyOldPatch(app)
        if (cleanOldPatches.isError)
            return cleanOldPatches

        val cleanPatches = Patcher.destroyPatch(app)
        if (cleanOldPatches.isError)
            return cleanPatches

        return PackageManagerResult.Success(null)
    }

    inline fun prepareStock(
        stockPackage: String,
        stockVersion: String,
        install: () -> PackageManagerResult<Nothing>
    ): PackageManagerResult<Nothing> {
        val stockYoutubeVersion = PMRoot.getPackageVersionName(stockPackage)
            .getOrElse { null }
        if (stockYoutubeVersion != stockVersion) {
            val uninstallStock = PMRoot.uninstallApp(stockPackage)
            if (uninstallStock.isError)
                return uninstallStock

            val installStock = install()
            if (installStock.isError)
                return installStock
        }

        return PackageManagerResult.Success(null)
    }

    fun patchStock(
        patchPath: String,
        stockPackage: String,
        app: String
    ): PackageManagerResult<Nothing> {
        val movePatch = Patcher.movePatchToDataAdb(patchPath, app)
        if (movePatch.isError)
            return movePatch

        val chconPatch = Patcher.chconPatch(app)
        if (chconPatch.isError)
            return chconPatch

        val stockPackageDir = PMRoot.getPackageDir(stockPackage)
            .getOrElse { error -> return error }!!

        val setupScript = Patcher.setupScript(app, stockPackage, stockPackageDir)
        if (setupScript is PackageManagerResult.Error)
            return setupScript

        val linkPatch = Patcher.linkPatch(app, stockPackage, stockPackageDir)
        if (linkPatch is PackageManagerResult.Error)
            return linkPatch

        return PackageManagerResult.Success(null)
    }

}