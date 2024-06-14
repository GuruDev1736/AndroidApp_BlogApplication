package com.gurudev.blog_application.ResponseModel

data class getPostByIdResponseModel(
    val addedDate: Long,
    val categories: Categories,
    val comments: List<Comment>,
    val content: String,
    val imageUrl: Any,
    val postId: Int,
    val title: String,
    val user: User
)