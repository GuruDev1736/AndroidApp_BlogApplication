package com.gurudev.blog_application.ResponseModel

data class CommentResponseModel(
    val commentId: Int,
    val content: String,
    val user: User
)