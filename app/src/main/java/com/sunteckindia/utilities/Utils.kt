package com.sunteckindia.utilities

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.io.File

object Utils {
    private val classTag: String = Utils::class.java.simpleName

    fun isStringNull(data: String?): Boolean {
        if (data == null || data.isEmpty() || data.equals(
                "null",
                ignoreCase = true
            ) || data.equals("")
        ) {
            return true
        }
        return false
    }

    fun checkFileSize(
        context: Context,
        fileUri: Uri?
    ): Float {
        try {
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor =
                context.contentResolver.query(fileUri!!, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            LogUtils.logE("filepath>>>>", picturePath)
            val file = File(picturePath)
            val fileSizeInBytes = file.length().toFloat()
            val fileSizeInKB = fileSizeInBytes / 1024
            val fileSizeInMB = fileSizeInKB / 1024
            LogUtils.logE(classTag, "====> Selected File Size : $fileSizeInMB")
            return fileSizeInKB
        } catch (ex: Exception) {
            LogUtils.logE(classTag, ex)
        }
        return 0f
    }

    fun getDataColumn(
        context: Context, uri: Uri?,
        selection: String?, selectionArgs: Array<String?>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return ""
    }

    fun checkFileSizeFromPath(path: String?): Float {
        try {
            val file = File(path)
            val fileSizeInBytes = file.length().toFloat()
            return fileSizeInBytes / 1024
        } catch (ex: java.lang.Exception) {
            LogUtils.logE(classTag, ex)
        }
        return 0f
    }
}