package com.isyed.githubber

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.isyed.githubber.model.GithubNetwork

class MyApplication : Application() {


    companion object{

        lateinit var myAppContext : Context
        lateinit var githubApi : GithubAPI
        lateinit var githubNetwork : GithubNetwork

        fun isNetworkAvailable(): Boolean{

            val connectivityManager =
                myAppContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    override fun onCreate() {
        super.onCreate()
        myAppContext = applicationContext
        githubApi = GithubAPI.initRetrofit()
        githubNetwork = GithubNetwork.getInstance()!!
    }

    fun getAppContext(): Context {
        return myAppContext
    }

}
