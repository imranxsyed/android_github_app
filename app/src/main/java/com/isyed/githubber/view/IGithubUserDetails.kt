package com.isyed.githubber.view

import com.isyed.githubber.GithubRepo
import com.isyed.githubber.GithubUser

interface IGithubUserDetails{
    fun onBind()
    fun getUserInfo()
    fun postGetUserInfo(githubUser: GithubUser)
    fun getUserRepos()
    fun postGetUserRepos(repos : List<GithubRepo>)
}