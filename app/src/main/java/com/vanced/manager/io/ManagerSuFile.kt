package com.vanced.manager.io

import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.vanced.manager.util.errString
import com.vanced.manager.util.outString
import java.io.File

class ManagerSuFile : SuFile {

    sealed class SuFileResult {
        data class Success(val output: String) : SuFileResult()
        data class Error(val error: String) : SuFileResult()
    }

    constructor(pathName: String) : super(pathName)
    constructor(parent: String, child: String) : super(parent, child)
    constructor(parent: File, child: String) : super(parent, child)

    private fun cmd(input: String): SuFileResult {
        val cmd = Shell.su(input.replace("@@", escapedPath)).exec()
        if (!cmd.isSuccess)
            return SuFileResult.Error(cmd.errString)

        return SuFileResult.Success(cmd.outString)
    }

    override fun delete(): Boolean {
        val result = cmd("rm -f @@ || rmdir -f @@")
        if (result is SuFileResult.Error)
            throw SUIOException(result.error)

        return true
    }
}