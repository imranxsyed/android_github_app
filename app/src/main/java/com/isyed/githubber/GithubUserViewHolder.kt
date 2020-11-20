package com.isyed.githubber

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.github_user_layout.view.*


class GithubUserViewHolder(private val githubUserView:View) : RecyclerView.ViewHolder(githubUserView){

    val imgViewGithubUserImage : ImageView = githubUserView.github_user_layout_user_image
    val tvUserName : TextView = githubUserView.findViewById(R.id.github_user_layout_user_name)
//    val tvUserRepoNum :TextView = employeeView.findViewById(R.id.github_user_layout_repo_number)

    fun onBind(githubUser: GithubUser , githubUserClickedCallback: (githubUser : GithubUser)-> Unit){
        tvUserName.text  = githubUser.login
        Picasso.get().load(Uri.parse( githubUser.avatar_url)).into(imgViewGithubUserImage)
        githubUserView.setOnClickListener{githubUserClickedCallback.invoke(githubUser)}
    }
}