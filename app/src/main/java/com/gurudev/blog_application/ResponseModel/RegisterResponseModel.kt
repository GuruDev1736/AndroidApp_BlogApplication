package com.gurudev.blog_application.ResponseModel

data class RegisterResponseModel(
    val about: String,
    val email: String,
    val id: Int,
    val name: String,
    val password: String
)