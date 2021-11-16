package com.sunteckindia.ui.viewmodels

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunteckindia.database.entity.UserEntity
import com.sunteckindia.rootmanager.BaseViewModel
import com.sunteckindia.rootmanager.EventResponse
import com.sunteckindia.rootmanager.EventType
import com.sunteckindia.rootmanager.Resource
import com.sunteckindia.utilities.Utils

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}

class LoginViewModel : BaseViewModel(), Observable {


    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val inputPass = MutableLiveData<String>()

    @Bindable
    val errorEmail = MutableLiveData<String>()

    @Bindable
    val errorPass = MutableLiveData<String>()

    init {
        errorPass.value = ""
        errorEmail.value = ""
    }

    fun onSaveClick() {
        if (Utils.isStringNull(inputEmail.value)) {
            errorEmail.value = "PLease Enter Valid Email Address"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            errorEmail.value = "PLease Enter Valid Email Address"
        } else if (Utils.isStringNull(inputPass.value)) {
            errorPass.value = "PLease Enter Valid Password"
        } else if (inputPass.value!!.length < 5) {
            errorPass.value = "PLease Enter Valid Password. Must be 5 Character Long."
        } else {
            eventResponse.value = EventResponse(EventType.LOGIN_SUCCESS, "Great", "Done")
        }


    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}