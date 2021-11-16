package com.sunteckindia.rootmanager

data class EventResponse(val eventType: EventType?, val data: Any?, val msg: String?) {
    constructor() : this(null, null, null)
}


enum class EventType {
    LOGIN_SUCCESS,
    LOGIN_ERROR,
    LOGIN_LOADING,
    REGISTER_SUCCESS,
    REGISTER_ERROR,
    REGISTER_LOADING,
}