package com.mvvm.documentmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.mvvm.R
import com.mvvm.extensions.snack
import com.mvvm.utilities.AlertDialogManager
import com.mvvm.utilities.LogUtils
import com.mvvm.utilities.ProgressBar
import com.mvvm.utilities.Utils
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.NormalFilePickActivity
import com.vincent.filepicker.filter.entity.NormalFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class DocumentManager {
    private val classTag = DocumentManager::class.java.simpleName
    var mContext: Context
    var documentType: DocumentType
    private var arrDocFiles: MutableList<String>? = null
    private var progressBar: ProgressBar? = null
    private val listener: OnDocumentSelectedListener
    private var dialog: AppCompatDialog? = null

    companion object {
        const val FROM_CAMERA: Int = 0
        const val FROM_GALLERY: Int = 1
    }

    constructor(
        mContext: Context,
        documentType: DocumentType,
        listener: OnDocumentSelectedListener
    ) {
        this.mContext = mContext
        this.documentType = documentType
        progressBar = ProgressBar("", true, mContext)
        this.listener = listener
    }

    fun showOptionsDialog() {
        dialog = AlertDialogManager.getCustomDialogWithTransparentBack(
            mContext,
            R.layout.dialog_document_options
        )

        val tvUploadFile =
            dialog?.findViewById<AppCompatTextView>(R.id.tvUploadFile) as AppCompatTextView
        val tvChooseImage =
            dialog?.findViewById<AppCompatTextView>(R.id.tvChooseImage) as AppCompatTextView
        val tvCaptureImage =
            dialog?.findViewById<AppCompatTextView>(R.id.tvCaptureImage) as AppCompatTextView
        val ivClose = dialog?.findViewById<AppCompatImageView>(R.id.ivClose)
        ivClose?.setOnClickListener {
            dialog?.dismiss()
        }
        tvUploadFile.setOnClickListener {
            chooseFromFile()
        }
        tvChooseImage.setOnClickListener {
            chooseFromGallery()
        }
        tvCaptureImage.setOnClickListener {
            captureFromCamera()
        }
        dialog?.show()

    }

    fun hideOptionsDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    fun chooseFromGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        (mContext as AppCompatActivity).startActivityForResult(pickPhoto, FROM_GALLERY)
    }

    fun captureFromCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        (mContext as AppCompatActivity).startActivityForResult(takePicture, FROM_CAMERA)
    }

    fun chooseFromFile() {
        val intent4 = Intent(mContext, NormalFilePickActivity::class.java)
        intent4.putExtra(Constant.MAX_NUMBER, 1)
        intent4.putExtra(
            NormalFilePickActivity.SUFFIX,
            arrayOf("xlsx", "xls", "doc", "docx", "ppt", "pptx", "txt", "pdf")
        )
        (mContext as AppCompatActivity).startActivityForResult(
            intent4,
            Constant.REQUEST_CODE_PICK_FILE
        )

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (resultCode == Activity.RESULT_OK && data != null) {

                hideOptionsDialog()
                if (requestCode == FROM_CAMERA) {
                    onCaptureImageResult(data)
                } else if (requestCode == FROM_GALLERY) {
                    onSelectFromGalleryResult(data)
                } else if (requestCode == Constant.REQUEST_CODE_PICK_FILE) {
                    if (arrDocFiles == null)
                        arrDocFiles = ArrayList()

                    val list =
                        data.getParcelableArrayListExtra<NormalFile>(Constant.RESULT_PICK_FILE)
                    for (file in list!!) {
                        val path = file.path
                        arrDocFiles?.add(path)
                    }
                    onSelectFromFileManager((arrDocFiles as ArrayList<String>?)!!)
                }
            }
        } catch (ex: Exception) {
            LogUtils.logE(classTag, ex)
        }

    }

    private fun onCaptureImageResult(data: Intent) {

        val thumbnail = data.extras!!.get("data") as Bitmap?

        if (thumbnail != null) {
            val bytes = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            val fileSizeInBytes = bytes.size().toFloat()
            val fileSizeInKB = fileSizeInBytes / 1024
            val fileSizeInMB = fileSizeInKB / 1024

            if (fileSizeInMB > 5) {
                ((mContext as AppCompatActivity)).window.decorView.findViewById<View>(android.R.id.content)
                    .snack(mContext.resources.getString(R.string.max_size_validate)) {}
                return
            }
            var fo: FileOutputStream? = null
            var destination: File? = null
            try {
                destination = File(
                    mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "IMG_Demo_App_" + System.currentTimeMillis() + ".jpg"
                )

                if (destination.createNewFile()) {
                    fo = FileOutputStream(destination)
                    fo.write(bytes.toByteArray())
                    fo.close()
                }

            } catch (ex: Exception) {
                LogUtils.logE(classTag, ex)
            } finally {
                if (fo != null) {
                    try {
                        fo.close()
                    } catch (e: IOException) {
                        LogUtils.logE(classTag, e)
                    }

                }
            }
            listener.onDocumentSelected(documentType, destination?.path)


        } else {
            ((mContext as AppCompatActivity)).window.decorView.findViewById<View>(android.R.id.content)
                .snack(mContext.resources.getString(R.string.generic_error)) {}
        }
    }

    private fun onSelectFromGalleryResult(data: Intent?) {

        var bm: Bitmap? = null
        try {
            if (data != null) {
                val fileSizeInKB = Utils.checkFileSize(mContext, data.data)
                if (fileSizeInKB / 1024 > 5) {
                    ((mContext as AppCompatActivity)).window.decorView.findViewById<View>(android.R.id.content)
                        .snack(mContext.resources.getString(R.string.max_size_validate)) {}
                    return
                }

                var path: String? = ""
                var filename = ""

                try {
                    val uri = data.data
                    if ("content".equals(uri!!.scheme!!, ignoreCase = true)) {
                        path = Utils.getDataColumn(mContext, uri, null, null)
                        filename = path!!.substring(path.lastIndexOf('/') + 1)
                    } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                        path = uri.path
                        filename = path!!.substring(path.lastIndexOf('/') + 1)
                    }

                } catch (e: Exception) {
                    LogUtils.logE(classTag, e)
                }

                listener.onDocumentSelected(documentType, path)

            }
        } catch (ex: Exception) {
            LogUtils.logE(classTag, ex)
        }

    }

    private fun onSelectFromFileManager(arrFilePaths: ArrayList<String>) {

        var currentFilePath = ""
        try {
            if (!arrFilePaths.isEmpty()) {
                for (i in arrFilePaths.indices) {
                    currentFilePath = arrFilePaths[i]

                    val fileSizeInKB = Utils.checkFileSizeFromPath(currentFilePath)
                    if (fileSizeInKB / 1024 > 5) {
                        ((mContext as AppCompatActivity)).window.decorView.findViewById<View>(
                            android.R.id.content
                        ).snack(mContext.resources.getString(R.string.max_size_validate)) {}

                        return
                    }

                    var filename = ""
                    var fileExt = ""

                    try {
                        filename = currentFilePath.substring(currentFilePath.lastIndexOf('/') + 1)
                        fileExt = filename.substring(filename.lastIndexOf('.'))
                    } catch (e: Exception) {
                        LogUtils.logE(classTag, e)

                    }

                    listener.onDocumentSelected(documentType, currentFilePath)

                }

            }
        } catch (ex: Exception) {
            LogUtils.logE(classTag, ex)
        } finally {
            arrDocFiles?.clear()
        }
    }


}