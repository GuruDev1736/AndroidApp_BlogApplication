package com.gurudev.blog_application.ResponseModel

data class Comment(
    val commentId: Int,
    val content: String,
    val user: User
)