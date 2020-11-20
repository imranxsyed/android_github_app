package com.isyed.githubber

import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.nio.charset.StandardCharsets
import java.util.*

interface GithubAPI {


    @GET(SEARCH_USER_REPO_END_POINT)
    fun getUserRepos(
        @Header("Authorization") us:String,
        @Path("user") userName : String
    ): Call<List<GithubRepo>>

    @GET(SEARCH_USER_END_POINT)
    fun getUsers(
        @Query("q") userName : String): Call<GithubUserResponse>

    @GET(SEARCH_SPECIFIC_USER_END_POINT)
    fun getSpecificUserByLogin(
        @Path("user_login") userLogin : String
    ): Call<GithubUser>

    companion object{
        /*
        https://api.github.com/search/users?q=<user_name>
        https://api.github.com/users/{user}/repos
        https://api.github.com/users/{imranxsyed}
         */

        const val SEARCH_USER_REPO_END_POINT = "/users/{user}/repos"
        const val SEARCH_USER_END_POINT = "/search/users"
        const val SEARCH_SPECIFIC_USER_END_POINT = "/users/{user_login}"
        const val BASE_URL = "https://api.github.com"


        fun initRetrofit(): GithubAPI {

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubAPI::class.java)

        }
    }
}
