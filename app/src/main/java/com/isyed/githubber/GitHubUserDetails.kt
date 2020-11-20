package com.isyed.githubber

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_git_hub_user_details.*
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Response

class GitHubUserDetails : AppCompatActivity() {
    lateinit var myUser : GithubUser
    lateinit var reposAdapter: GithubAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_hub_user_details)

        getUserInfo()
    }
    private fun getUserInfo(){
        if (!MyApplication.isNetworkAvailable()){
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_LONG).show()
            return
        }

         myUser = intent?.getParcelableExtra<GithubUser>(MainActivity.EX_GITHUB_USER)!!

        MyApplication.githubApi.getSpecificUserByLogin(myUser.login).enqueue(
            object : retrofit2.Callback<GithubUser>{
                override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                    println(t)
                }

                override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            myUser = it
                            myUser.userRepos = mutableListOf()
                            populateData()
                        }
                    }
                }

            }
        )
    }

    private fun populateData(){

        activity_git_hub_user_details_user_name.text =  myUser.name
        activity_git_hub_user_details_user_email.text = MyApplication.myAppContext.getString(R.string.email).replace("{email}",myUser.email ?: "No Email Found")
        activity_git_hub_user_details_user_location.text = MyApplication.myAppContext.getString(R.string.location).replace("{location}", myUser.location ?: "No Location Associated")
        activity_git_hub_user_details_user_join_date.text =  MyApplication.myAppContext.getString(R.string.join_date).replace("{date}", myUser.created_at ?: "No Join Data Found")
        activity_git_hub_user_details_user_followers.text =
            MyApplication.myAppContext.getString(R.string._0_followers).replace("0", myUser.followers.toString())
        activity_git_hub_user_details_user_following.text =
            MyApplication.myAppContext.getString(R.string.following).replace("0",myUser.following.toString())
        activity_git_hub_user_details_user_bio.text = myUser.bio ?: "No Bio Available"
        Picasso.get().load(Uri.parse(myUser.avatar_url)).into(activity_git_hub_user_details_iv_user_image)

        activity_git_hub_user_details_root.visibility = View.VISIBLE

//        if (myUser.public_repos > 0){
//           activity_git_hub_user_details_search_bar.visibility = View.VISIBLE
//            activity_git_hub_user_details_rv_repos.visibility = View.VISIBLE
//            activity_git_hub_user_details_repo_msg.visibility = View.INVISIBLE
//            getUserRepos()
//
//        }
    }
    private fun getUserRepos(){
        MyApplication.githubApi.getUserRepos(
            Credentials.basic("imranxsyed", "f5f1fb478578a6be5122cebc155a119a902f4c51"),
            myUser.login).enqueue(
            object : retrofit2.Callback<List<GithubRepo>> {
                override fun onFailure(call: Call<List<GithubRepo>>, t: Throwable) { println(t)}
                override fun onResponse(call: Call<List<GithubRepo>>, response: Response<List<GithubRepo>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { repos ->
                            myUser.userRepos = repos
                            //setting the repos
                            //TODO add a recyclerview for repositories
                            (activity_git_hub_user_details_rv_repos as RecyclerView)

                        }
                    }
                }
            })
    }
}