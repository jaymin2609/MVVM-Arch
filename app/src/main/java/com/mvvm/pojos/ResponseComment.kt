package com.mvvm.pojos

data class ResponseComment(
    val comments: List<Comment>

)

data class Comment(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String,
    )
