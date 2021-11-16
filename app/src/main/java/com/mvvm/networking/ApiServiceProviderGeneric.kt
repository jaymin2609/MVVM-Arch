package com.mvvm.networking

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mvvm.BuildConfig
import com.mvvm.R
import com.mvvm.rootmanager.ResourceProvider
import com.mvvm.utilities.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import javax.inject.Inject

class ApiServiceProviderGeneric @Inject constructor(
) : APIClient() {
    private val classTag = javaClass.simpleName
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)
    private var apiResponseCallBack: ApiResponseCallBack? = null

    constructor(apiResponseCallBack: ApiResponseCallBack) : this() {
        this.apiResponseCallBack = apiResponseCallBack
    }

    fun setResponseCallBack(apiResponseCallBack: ApiResponseCallBack) {
        this.apiResponseCallBack = apiResponseCallBack
    }


    fun postCall(
        jsonObj: JsonObject,
        returnType: ReturnType
    ) {
        coroutineScope.launch {
            try {
                launch(Dispatchers.Main) {
                    apiResponseCallBack?.apply {
                        onPreExecute(returnType)
                    }
                }
                val call =
                    getClientWithHeader(ResourceProvider.applicationContext).create(GetCallReference::class.java)
                        .postCall(BuildConfig.BASE_URL + returnType.endPoint, jsonObj)
                launch(Dispatchers.Main) {
                    if (call.body() != null && call.isSuccessful) {
                        LogUtils.logE(classTag, "response : ${call.body() as JsonElement}")
                        apiResponseCallBack?.apply {
                            onSuccess(
                                returnType, call.body().toString()
                            )
                        }
                    } else {
                        apiResponseCallBack?.apply {
                            onError(
                                returnType,
                                ResourceProvider.applicationContext.getString(R.string.msg_unable_connect_with_server)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    apiResponseCallBack?.apply {
                        onError(
                            returnType,
                            ResourceProvider.applicationContext.getString(R.string.msg_unable_connect_with_server)
                        )
                    }
                }
                LogUtils.logE(classTag, e)
            }
        }
    }

    fun postCallUrl(
        jsonObj: JsonObject,
        returnType: ReturnType
    ) {
        coroutineScope.launch {
            try {
                launch(Dispatchers.Main) {
                    apiResponseCallBack?.apply {
                        onPreExecute(returnType)
                    }
                }
                val call =
                    getClientWithHeader(ResourceProvider.applicationContext).create(GetCallReference::class.java)
                        .postCall(returnType.endPoint, jsonObj)
                launch(Dispatchers.Main) {
                    if (call.body() != null && call.isSuccessful) {
                        LogUtils.logE(classTag, "response : ${call.body() as JsonElement}")
                        apiResponseCallBack?.apply {
                            onSuccess(
                                returnType, call.body().toString()
                            )
                        }
                    } else {
                        apiResponseCallBack?.apply {

                            onError(
                                returnType,
                                ResourceProvider.applicationContext.getString(R.string.msg_unable_connect_with_server)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    apiResponseCallBack?.apply {
                        onError(
                            returnType,
                            ResourceProvider.applicationContext.getString(R.string.msg_unable_connect_with_server)
                        )
                    }
                }
                LogUtils.logE(classTag, e)
            }
        }
    }

    fun getCallUrl(
        returnType: ReturnType
    ) {
        coroutineScope.launch {
            try {
                launch(Dispatchers.Main) {
                    apiResponseCallBack?.apply { onPreExecute(returnType) }
                }
                val call = getClientUrl(returnType.endPoint).create(GetCallReference::class.java)
                    .getCall(returnType.endPoint.substringAfterLast("/"))
                launch(Dispatchers.Main) {
                    if (call.body() != null && call.isSuccessful) {
                        LogUtils.logE(classTag, "response : ${call.body() as JsonElement}")
                        apiResponseCallBack?.apply {
                            onSuccess(returnType, call.body().toString())
                        }
                    } else {
                        apiResponseCallBack?.apply {
                            onError(
                                returnType,
                                ResourceProvider.applicationContext.getString(R.string.msg_unable_connect_with_server)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    apiResponseCallBack?.apply {
                        onError(
                            returnType,
                            ResourceProvider.applicationContext.getString(R.string.msg_unable_connect_with_server)
                        )
                    }
                }
                LogUtils.logE(classTag, e)
            }
        }
    }

    internal interface GetCallReference {
        @POST
        suspend fun postCall(
            @Url url: String,
            @Body jsonObject: JsonObject
        ): Response<JsonElement>

        @GET
        suspend fun getCall(
            @Url url: String
        ): Response<JsonElement>
    }
}
