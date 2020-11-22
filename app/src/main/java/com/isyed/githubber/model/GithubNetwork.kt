package com.isyed.githubber.model

import com.isyed.githubber.GithubUser
import com.isyed.githubber.GithubUserResponse
import com.isyed.githubber.MyApplication

class GithubNetwork private constructor(){


    companion object{
        @Volatile
        private var INSTANCE : GithubNetwork? = null
        fun getInstance(): GithubNetwork?{
            if (INSTANCE != null){
                return INSTANCE
            }
            synchronized(this){

                if (INSTANCE == null){
                    INSTANCE = GithubNetwork()
                }
                return INSTANCE
            }
        }
    }

    fun getGithubUsers(name : String,githubUsersCallback:retrofit2.Callback<GithubUserResponse>) {
        MyApplication.githubApi.getUsers(name).enqueue(githubUsersCallback)
    }

    fun getUserInfo(login : String, getUserInfoCallback: retrofit2.Callback<GithubUser>){
        MyApplication.githubApi.getSpecificUserByLogin(login).enqueue(getUserInfoCallback)
    }


}