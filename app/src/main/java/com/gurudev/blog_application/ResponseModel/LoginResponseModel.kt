package com.gurudev.blog_application.ResponseModel

data class LoginResponseModel(
    val token: String,
    val userName: String,
    val userId: Int,
    val userRole : String
)