package com.gurudev.blog_application.RequestModel

data class RegisterRequestModel(
    val about: String,
    val email: String,
    val name: String,
    val password: String
)