package com.mvvm.ui.viewmodels

import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mvvm.database.entity.UserEntity
import com.mvvm.database.repository.UserRepository
import com.mvvm.networking.ApiResponseCallBack
import com.mvvm.networking.ApiServiceProviderGeneric
import com.mvvm.networking.ReturnType
import com.mvvm.pojos.Comment
import com.mvvm.rootmanager.BaseViewModel
import com.mvvm.rootmanager.SealedResource
import com.mvvm.utilities.LogUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


//@HiltViewModel
class UserListViewModel @AssistedInject constructor(
    private val userRepository: UserRepository,
    private val apiServiceProviderGeneric: ApiServiceProviderGeneric,
    @Assisted
    private val userId: String

) : BaseViewModel(), ApiResponseCallBack {

    @AssistedFactory
    interface UserListViewModelFactory {
        fun create(userId: String): UserListViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun providesFactory(
            assistedFactory: UserListViewModelFactory,
            userId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(userId) as T
            }
        }
    }

    private val classTag = this::class.java.simpleName

    val userListLocal = userRepository.getUserData()


    private val _userList =
        MutableLiveData<SealedResource<List<UserEntity>>>().apply { value = SealedResource.None() }

    val userList: LiveData<SealedResource<List<UserEntity>>>
        get() = _userList

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    init {
        LogUtils.logE(classTag,"User ID : $userId")
        viewModelScope.launch {
            userRepository
                .insert(UserEntity(name = "Shiv", age = 28000))
        }
        apiServiceProviderGeneric.setResponseCallBack(this)
        callUsersApi()
    }

    private fun callUsersApi() {
        apiServiceProviderGeneric.getCall(
            ReturnType.GET_USERS
        )
        val map = mapOf("postId" to "1")
        apiServiceProviderGeneric.getCallUrlQuery(
            ReturnType.GET_COMMENTS_QUERY,
            map
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
                LogUtils.logE(classTag, "Response GET_USERS $response")
                val responseUsers = Gson().fromJson<List<UserEntity>>(
                    response,
                    object : TypeToken<List<UserEntity>>() {}.type
                )
                LogUtils.logE(classTag, "Size of users from GET_USERS ${responseUsers.size}")
                _userList.value = SealedResource.Success(responseUsers)
            } else if (returnType == ReturnType.GET_COMMENTS_QUERY) {
                LogUtils.logE(classTag, "Response GET_COMMENTS_QUERY $response")
                val responseComment = Gson().fromJson<List<Comment>>(
                    response,
                    object : TypeToken<List<Comment>>() {}.type
                )
                LogUtils.logE(
                    classTag,
                    "Size of users from GET_COMMENTS_QUERY ${responseComment.size}"
                )
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