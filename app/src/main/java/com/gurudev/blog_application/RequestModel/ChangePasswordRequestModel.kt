package com.gurudev.blog_application.RequestModel

data class ChangePasswordRequestModel(
    val newPassword: String,
    val oldPassword: String
)