package com.mvvm.rootmanager

sealed class SealedResource<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : SealedResource<T>(Status.SUCCESS, data)
    class Error<T>(data: T?=null, message: String?) : SealedResource<T>(Status.ERROR, data, message)
    class Loading<T>(data: T?=null, message: String?) : SealedResource<T>(Status.LOADING, data, message)
    class None<T> : SealedResource<T>(Status.NONE)
}

/*sealed class SealedResource {
    class Success(data: T) : SealedResource()
    class Error(data: T?, message: String?) : SealedResource()
    class Loading<T>(data: T?, message: String?) : SealedResource()
    class None<T>() : SealedResource()
}*/
