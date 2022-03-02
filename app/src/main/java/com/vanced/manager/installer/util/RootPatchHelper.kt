package com.vanced.manager.installer.util

object RootPatchHelper {

    fun cleanPatches(app: String): PMRootResult<Nothing> {
        val cleanOldPatches = Patcher.destroyOldPatch(app)
        if (cleanOldPatches.isError)
            return cleanOldPatches

        val cleanPatches = Patcher.destroyPatch(app)
        if (cleanOldPatches.isError)
            return cleanPatches

        return PMRootResult.Success()
    }

    inline fun prepareStock(
        stockPackage: String,
        stockVersion: String,
        install: () -> PMRootResult<Nothing>
    ): PMRootResult<Nothing> {
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

        return PMRootResult.Success()
    }

    fun patchStock(
        patchPath: String,
        stockPackage: String,
        app: String
    ): PMRootResult<Nothing> {
        val movePatch = Patcher.movePatchToDataAdb(patchPath, app)
        if (movePatch.isError)
            return movePatch

        val chconPatch = Patcher.chconPatch(app)
        if (chconPatch.isError)
            return chconPatch

        val stockPackageDir = PMRoot.getPackageDir(stockPackage)
            .getOrElse { error -> return error }!!

        val setupScript = Patcher.setupScript(app, stockPackage, stockPackageDir)
        if (setupScript is PMRootResult.Error)
            return setupScript

        val linkPatch = Patcher.linkPatch(app, stockPackage, stockPackageDir)
        if (linkPatch is PMRootResult.Error)
            return linkPatch

        return PMRootResult.Success()
    }

}