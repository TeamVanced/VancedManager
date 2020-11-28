package com.vanced.manager.feature.home.data.datasource

import com.vanced.manager.feature.home.data.pkg.PkgManager
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class PkgInformationDataSourceImplTest : ShouldSpec() {

    private val pkgManager: PkgManager = mockk()

    private val dataSource = PkgInformationDataSourceImpl(pkgManager)

    init { // https://kotest.io/styles/
        should("return version code") {
            val testPackageName = "VancedManager"
            val expectation = 4545
            coEvery { pkgManager.getVersionCode(testPackageName) } returns expectation
            dataSource.getVersionCode(testPackageName) shouldBe expectation
        }
        should("return version name") {
            val testPackageName = "VancedManager"
            val expectation = "VancedManagerName"
            coEvery { pkgManager.getVersionName(testPackageName) } returns expectation
            dataSource.getVersionName(testPackageName) shouldBe expectation
        }
    }
}