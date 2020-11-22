package com.isyed.githubber.view

import com.isyed.githubber.GithubUser

interface IMainActivity{

     fun getGithubUsers(name : String)
     fun bind()
     fun unBind()
     fun postGetGithubUsers(githubUsers: List<GithubUser>)
}