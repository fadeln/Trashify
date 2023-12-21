package com.example.trashify.data.retrofit

import com.example.trashify.data.response.CraftingResponse
import com.example.trashify.data.response.LoginResponse
import com.example.trashify.data.response.PostResponse
import com.example.trashify.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @GET("tutorial/glass")
    suspend fun getTutorial(
        @Path("material") material: String
    ): CraftingResponse

//    @GET("tutorial/glass")
//    suspend fun getTutorial(
//        @Path("material") material: String
//    ): Tutorial
}

interface ApiServicePost {

    @GET("stories")
    suspend fun getStory(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null,
    ): Call<PostResponse>

    @GET("stories")
    suspend fun getStoriesPaging(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null,

        ) : PostResponse

    @Multipart
    @POST("stories")
    fun addStories(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
    ) : Call<PostResponse>
}