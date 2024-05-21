package com.gurudev.blog_application.ResponseModel

data class PostResponseModel(
    val addedDate: Long,
    val categories: Categories,
    val comments: List<Any>,
    val content: String,
    val imageUrl: String,
    val postId: Int,
    val title: String,
    val user: User
)