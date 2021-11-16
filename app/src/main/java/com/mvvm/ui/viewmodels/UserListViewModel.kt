package com.mvvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mvvm.database.entity.UserEntity
import com.mvvm.database.repository.UserRepository
import com.mvvm.networking.ApiResponseCallBack
import com.mvvm.networking.ApiServiceProviderGeneric
import com.mvvm.networking.ReturnType
import com.mvvm.rootmanager.BaseViewModel
import com.mvvm.rootmanager.SealedResource
import com.mvvm.utilities.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,

    private val apiServiceProviderGeneric: ApiServiceProviderGeneric
) : BaseViewModel(), ApiResponseCallBack {

    private val classTag = this::class.java.simpleName

    val userListLocal = userRepository.getUserData()


    private val _userList =
        MutableLiveData<SealedResource<List<UserEntity>>>().apply { value = SealedResource.None() }

    val userList: LiveData<SealedResource<List<UserEntity>>>
        get() = _userList

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    init {
        viewModelScope.launch {
            userRepository
                .insert(UserEntity(name = "Shiv", age = 28000))
        }
        apiServiceProviderGeneric.setResponseCallBack(this)
        callUsersApi()
    }

    private fun callUsersApi() {
        apiServiceProviderGeneric.getCallUrl(
            ReturnType.GET_USERS
        )
    }

    override fun onPreExecute(returnType: ReturnType) {
        dataLoading.value = true
        _userList.value = SealedResource.Loading(message = null)
    }

    override fun onSuccess(returnType: ReturnType, response: String) {
        dataLoading.value = false
        try {
            if (returnType == ReturnType.GET_USERS) {
                val responseUsers = Gson().fromJson<List<UserEntity>>(
                    response,
                    object : TypeToken<List<UserEntity>>() {}.type
                )
                LogUtils.logE(classTag, "Size of users ${responseUsers.size}")
                _userList.value = SealedResource.Success(responseUsers)
            }
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    override fun onError(returnType: ReturnType, error: String) {
        dataLoading.value = false
        _userList.value = SealedResource.Error(message = error)
    }
}