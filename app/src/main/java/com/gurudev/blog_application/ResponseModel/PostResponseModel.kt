package com.gurudev.blog_application.ResponseModel

data class PostResponseModel(
    val content: List<Content>,
    val lastpage: Boolean,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int
)