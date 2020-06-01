package com.vanced.manager.utils

import java.io.File
import java.io.FileInputStream
import java.io.InputStream

open class FileInfo(val name: String, val fileSize: Long, val file: File? = null) {
    open fun getInputStream(): InputStream =
        if (file!= null)
            FileInputStream(file)
        else
            throw NotImplementedError("need some way to create InputStream")
}