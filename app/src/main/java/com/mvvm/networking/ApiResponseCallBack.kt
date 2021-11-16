package com.mvvm.networking

interface ApiResponseCallBack {
    fun onPreExecute(returnType: ReturnType)
    fun onSuccess(returnType: ReturnType, response: String)
    fun onError(returnType: ReturnType, error: String)
}