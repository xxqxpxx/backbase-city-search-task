package com.example.backbase.core.utils

import android.util.Log
import java.io.Closeable
import java.io.IOException

object StreamUtils {
    fun safeCloseCloseable(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                Log.e(StreamUtils::class.java.canonicalName, "IOException while closing stream", e)
            }
        }
    }
}