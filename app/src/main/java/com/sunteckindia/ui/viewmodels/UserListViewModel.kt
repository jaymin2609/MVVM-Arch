package com.sunteckindia.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunteckindia.database.entity.UserEntity
import com.sunteckindia.database.repository.UserRepository
import com.sunteckindia.networking.ApiResponseCallBack
import com.sunteckindia.networking.ApiServiceProviderGeneric
import com.sunteckindia.networking.ReturnType
import com.sunteckindia.rootmanager.BaseViewModel
import com.sunteckindia.rootmanager.Resource
import com.sunteckindia.utilities.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


/*@Suppress("UNCHECKED_CAST")
class UserListViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
            return UserListViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}*/
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,

    private val apiServiceProviderGeneric: ApiServiceProviderGeneric
) : BaseViewModel(), ApiResponseCallBack {

    private val classTag = this::class.java.simpleName

    val userListLocal = userRepository.getUserData()


    private val _userList =
        MutableLiveData<Resource<List<UserEntity>>>().apply { value = Resource.none() }

    val userList: LiveData<Resource<List<UserEntity>>>
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
        _userList.value = Resource.loading(null)
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
                _userList.value = Resource.success(responseUsers)
            }
        } catch (e: Exception) {
            LogUtils.logE(classTag, e)
        }
    }

    override fun onError(returnType: ReturnType, error: String) {
        dataLoading.value = false
        _userList.value = Resource.error(error, null)
    }
}