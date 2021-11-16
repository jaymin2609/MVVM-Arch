package com.sunteckindia.networking

enum class ReturnType(val endPoint: String) {
    POST_LOGIN("login"),
    POST_REGISTER("register"),
    GET_USERS("https://jsonplaceholder.typicode.com/users"),

}