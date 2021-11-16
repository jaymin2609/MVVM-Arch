package com.sunteckindia.rootmanager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val empty = MutableLiveData<Boolean>().apply { value = false }

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val eventResponse = MutableLiveData<EventResponse>()

    val toastMessage = MutableLiveData<String>()

}