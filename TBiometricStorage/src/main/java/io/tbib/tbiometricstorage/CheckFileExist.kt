package io.tbib.tbiometricstorage

import android.content.Context

    fun TBiometricStorage.fileExists(context: Context, filename: String): Boolean {
        val file = context.getFileStreamPath(filename)
        return file?.exists() ?: false
    }
