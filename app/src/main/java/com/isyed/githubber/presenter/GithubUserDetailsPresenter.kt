package com.isyed.githubber.presenter

import androidx.recyclerview.widget.RecyclerView
import com.isyed.githubber.GithubRepo
import com.isyed.githubber.GithubUser
import com.isyed.githubber.MyApplication
import com.isyed.githubber.view.IGithubUserDetails
import kotlinx.android.synthetic.main.activity_git_hub_user_details.*
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Response

class GithubUserDetailsPresenter(val view: IGithubUserDetails){


    fun getUserInfo(login : String){
        MyApplication.githubNetwork.getUserInfo(login,getUserInfoCallback)
//        MyApplication.githubApi.getSpecificUserByLogin(login).enqueue(getUserInfoCallback)
    }

    val getUserInfoCallback = object : retrofit2.Callback<GithubUser>{
        override fun onFailure(call: Call<GithubUser>, t: Throwable) {
            println(t)
        }

        override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
            if(response.isSuccessful){
                response.body()?.let {
                    view.postGetUserInfo(it)
                }
            }
        }

    }

    fun getUserRepos(login: String){
        MyApplication.githubApi.getUserRepos(
                Credentials.basic("imranxsyed", "f5f1fb478578a6be5122cebc155a119a902f4c51"),
                login).enqueue(
                object : retrofit2.Callback<List<GithubRepo>> {
                    override fun onFailure(call: Call<List<GithubRepo>>, t: Throwable) { println(t)}
                    override fun onResponse(call: Call<List<GithubRepo>>, response: Response<List<GithubRepo>>) {
                        if (response.isSuccessful) {
                            response.body()?.let { repos ->
                                view.postGetUserRepos(repos)
                            }
                        }
                    }
                })
    }


}