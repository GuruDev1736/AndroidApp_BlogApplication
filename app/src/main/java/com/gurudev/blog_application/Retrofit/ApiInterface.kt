package com.gurudev.blog_application.Retrofit

import com.gurudev.blog_application.RequestModel.ChangePasswordRequestModel
import com.gurudev.blog_application.RequestModel.LoginRequestModel
import com.gurudev.blog_application.RequestModel.RegisterRequestModel
import com.gurudev.blog_application.RequestModel.createPostRequestModel
import com.gurudev.blog_application.RequestModel.updateUserRequestModel
import com.gurudev.blog_application.ResponseModel.CategoryResponseModelItem
import com.gurudev.blog_application.ResponseModel.DefaultResponseModel
import com.gurudev.blog_application.ResponseModel.LoginResponseModel
import com.gurudev.blog_application.ResponseModel.PostListModel
import com.gurudev.blog_application.ResponseModel.PostResponseModel
import com.gurudev.blog_application.ResponseModel.ProfileResponse
import com.gurudev.blog_application.ResponseModel.RegisterResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("api/post/user/{userId}")
    fun getPostsById(@Path("userId") userId : Int):Call<PostListModel>

    @GET("api/user/{userId}")
    fun getUserById(@Path("userId") userId : Int):Call<ProfileResponse>


    @PUT("api/user/update/{userId}")
    fun updateUser( @Header("Authorization") token : String , @Path("userId") userId : Int , @Body model : updateUserRequestModel):Call<ProfileResponse>

    @PUT("api/user/changePass/{userId}")
    fun updatePassword( @Header("Authorization") token : String , @Path("userId") userId : Int , @Body model : ChangePasswordRequestModel):Call<DefaultResponseModel>

}