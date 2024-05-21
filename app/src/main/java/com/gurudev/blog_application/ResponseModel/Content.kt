package com.gurudev.blog_application.ResponseModel

data class Content(
    val addedDate: Long,
    val categories: CategoriesX,
    val comments: List<Any>,
    val content: String,
    val imageUrl: Any,
    val postId: Int,
    val title: String,
    val user: UserX
)