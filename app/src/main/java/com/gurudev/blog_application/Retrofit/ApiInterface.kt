package com.gurudev.blog_application.Retrofit

import com.gurudev.blog_application.RequestModel.LoginRequestModel
import com.gurudev.blog_application.RequestModel.RegisterRequestModel
import com.gurudev.blog_application.RequestModel.createPostRequestModel
import com.gurudev.blog_application.ResponseModel.CategoryResponseModelItem
import com.gurudev.blog_application.ResponseModel.LoginResponseModel
import com.gurudev.blog_application.ResponseModel.PostListModel
import com.gurudev.blog_application.ResponseModel.PostResponseModel
import com.gurudev.blog_application.ResponseModel.RegisterResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @POST("auth/register")
    fun register(@Body model : RegisterRequestModel):Call<RegisterResponseModel>

    @POST("auth/login")
    fun login(@Body model : LoginRequestModel):Call<LoginResponseModel>

    @GET("api/category/all")
    fun getCategory(@Header("Authorization") token : String):Call<List<CategoryResponseModelItem>>

    @POST("api/post/{userId}/{categoryId}")
    fun createPost(@Header("Authorization") token : String , @Path("userId") userId : Int , @Path("categoryId") categoryId : Int , @Body model : createPostRequestModel):Call<PostResponseModel>

    @GET("api/post/")
    fun getPosts():Call<PostListModel>



}