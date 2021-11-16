package com.mvvm.documentmanager

interface OnDocumentSelectedListener {
    fun onDocumentSelected(documentType: DocumentType?, imagePath: String?)
}