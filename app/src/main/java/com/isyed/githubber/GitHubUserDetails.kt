package com.isyed.githubber

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.isyed.githubber.presenter.GithubUserDetailsPresenter
import com.isyed.githubber.view.IGithubUserDetails
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_git_hub_user_details.*
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Response

class GitHubUserDetails : AppCompatActivity() , IGithubUserDetails{
    var presenter : GithubUserDetailsPresenter? = null
    lateinit var myUser : GithubUser
    lateinit var reposAdapter: GithubAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        onBind()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_hub_user_details)

        getUserInfo()
    }

    override fun onBind() {
        presenter = GithubUserDetailsPresenter(this)
    }

    override  fun getUserInfo(){
        if (!MyApplication.isNetworkAvailable()){
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_LONG).show()
            return
        }

         myUser = intent?.getParcelableExtra<GithubUser>(MainActivity.EX_GITHUB_USER)!!
         presenter?.getUserInfo(myUser.login)
    }

    override fun postGetUserInfo(githubUser: GithubUser) {
        populateData(githubUser)
    }



    override fun postGetUserRepos(repos: List<GithubRepo>) {

    }

    private fun populateData(githubUser : GithubUser){

        activity_git_hub_user_details_user_name.text =  githubUser.name
        activity_git_hub_user_details_user_email.text = MyApplication.myAppContext.getString(R.string.email).replace("{email}",githubUser.email ?: "No Email Found")
        activity_git_hub_user_details_user_location.text = MyApplication.myAppContext.getString(R.string.location).replace("{location}", githubUser.location ?: "No Location Associated")
        activity_git_hub_user_details_user_join_date.text =  MyApplication.myAppContext.getString(R.string.join_date).replace("{date}", githubUser.created_at ?: "No Join Data Found")
        activity_git_hub_user_details_user_followers.text =
            MyApplication.myAppContext.getString(R.string._0_followers).replace("0", githubUser.followers.toString())
        activity_git_hub_user_details_user_following.text =
            MyApplication.myAppContext.getString(R.string.following).replace("0",githubUser.following.toString())
        activity_git_hub_user_details_user_bio.text = githubUser.bio ?: "No Bio Available"
        Picasso.get().load(Uri.parse(githubUser.avatar_url)).into(activity_git_hub_user_details_iv_user_image)

        activity_git_hub_user_details_root.visibility = View.VISIBLE

//        if (myUser.public_repos > 0){
//           activity_git_hub_user_details_search_bar.visibility = View.VISIBLE
//            activity_git_hub_user_details_rv_repos.visibility = View.VISIBLE
//            activity_git_hub_user_details_repo_msg.visibility = View.INVISIBLE
//            getUserRepos()
//
//        }
    }
    override  fun getUserRepos(){
      presenter?.getUserInfo(myUser.login)
    }

}