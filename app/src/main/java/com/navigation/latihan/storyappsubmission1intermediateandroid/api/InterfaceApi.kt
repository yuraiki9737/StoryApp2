package com.navigation.latihan.storyappsubmission1intermediateandroid.api

import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.ResponseLoginStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.ResponseRegisterStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface InterfaceApi {
    @FormUrlEncoded
    @POST("login")
    suspend fun loginAkunStoryApp(
        @Field("email") email: String,
        @Field("password") pass: String
    ): ResponseLoginStory

    @FormUrlEncoded
    @POST("register")
    suspend fun registerAkunStoryApp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String

    ):ResponseRegisterStory


    @GET("stories")
    suspend fun getStoryApp(@Header("Authorization") token: String,
                            @Query("page") page: Int,
                            @Query("size") size: Int,): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun imageUploadStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("lat") latitude: RequestBody?,
        @Part("lon") longitude: RequestBody?,
        ): ResponseRegisterStory

    @GET("stories?location=1")
    suspend fun getStoryLocation(
        @Header("Authorization") token: String)
    :StoryResponse
}