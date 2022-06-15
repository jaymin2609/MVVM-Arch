package com.mvvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mvvm.networking.ApiResponseCallBack
import com.mvvm.networking.ApiServiceProviderGeneric
import com.mvvm.networking.ReturnType
import com.mvvm.pojos.ReqInspectionStages
import com.mvvm.pojos.ResInspectionStages
import com.mvvm.pojos.Stage
import com.mvvm.rootmanager.BaseViewModel
import com.mvvm.rootmanager.SealedResource
import com.mvvm.utilities.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject


@HiltViewModel
class InspectionStagesViewModel @Inject constructor(
    private val apiServiceProviderGeneric: ApiServiceProviderGeneric,

    ) : BaseViewModel(), ApiResponseCallBack {

    private val classTag = this::class.java.simpleName

    private val _inspectionStageList =
        MutableLiveData<SealedResource<List<Stage>>>().apply { value = SealedResource.None() }

    val inspectionStageList: LiveData<SealedResource<List<Stage>>>
        get() = _inspectionStageList

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    init {
        apiServiceProviderGeneric.setResponseCallBack(this)
        callStagesApi()
    }

    private fun callStagesApi() {
        val reqInspectionStages = ReqInspectionStages("52007")
        apiServiceProviderGeneric.postCallUrl(
            reqInspectionStages,
            ReturnType.POST_INSPECTION_STAGES
        )
    }

    override fun onPreExecute(returnType: ReturnType) {
        dataLoading.value = true
        _inspectionStageList.value = SealedResource.Loading(message = null)
    }

    override fun onSuccess(returnType: ReturnType, response: String) {
        dataLoading.value = false
        try {
            if (returnType == ReturnType.POST_INSPECTION_STAGES) {
                LogUtils.logE(classTag, "Response POST_INSPECTION_STAGES $response")
                val responseUsers = Gson().fromJson<ResInspectionStages>(
                    response,
                    object : TypeToken<ResInspectionStages>() {}.type
                )
                LogUtils.logE(
                    classTag,
                    "Size of users from POST_INSPECTION_STAGES ${responseUsers.extraData.size}"
                )
                _inspectionStageList.value = SealedResource.Success(responseUsers.extraData)
            }
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    override fun onError(returnType: ReturnType, error: String) {
        dataLoading.value = false
        _inspectionStageList.value = SealedResource.Error(message = error)
    }
}