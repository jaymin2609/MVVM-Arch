package com.mvvm.networking

enum class ReturnType(val endPoint: String) {
    POST_LOGIN("login"),
    POST_REGISTER("register"),
    GET_USERS_URL("https://jsonplaceholder.typicode.com/users"),
    GET_USERS("users"),
    GET_COMMENTS_QUERY("https://jsonplaceholder.typicode.com/comments?postId=1"),
}