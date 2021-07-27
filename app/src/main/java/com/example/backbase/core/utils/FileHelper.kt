package com.example.backbase.core.utils

import android.content.Context
import android.util.Log
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class FileHelper(private val context: Context) {
    fun read(@RawRes rawFile: Int): String {
        val output = StringBuilder()
        var inputStream: InputStream? = null
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        try {
            inputStream = context.resources.openRawResource(rawFile)
            inputStreamReader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
            bufferedReader = BufferedReader(inputStreamReader)
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                output.append(line)
                output.append("\n")
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.canonicalName, "Error while reading file from raw", e)
        } finally {
            StreamUtils.safeCloseCloseable(inputStream)
            StreamUtils.safeCloseCloseable(inputStreamReader)
            StreamUtils.safeCloseCloseable(bufferedReader)
        }
        return output.toString()
    }
}