package com.isyed.githubber.presenter

import com.isyed.githubber.GithubUser
import com.isyed.githubber.GithubUserResponse
import com.isyed.githubber.MyApplication
import com.isyed.githubber.view.IMainActivity
import retrofit2.Call
import retrofit2.Response

class MainActivityPresenter(val view : IMainActivity){

    fun getGithubUsers(name : String) {
        MyApplication.githubNetwork.getGithubUsers(name,githubUsersCallback)
    }

    private val githubUsersCallback  = object : retrofit2.Callback<GithubUserResponse>{
        override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
            view.postGetGithubUsers(mutableListOf())
        }

        override fun onResponse(call: Call<GithubUserResponse>, response: Response<GithubUserResponse>) {
            if (response.isSuccessful){
                response.body()?.let {
                    view.postGetGithubUsers(it.items)
                }

            }
            else{
                view.postGetGithubUsers(mutableListOf())
            }
        }
    }
}