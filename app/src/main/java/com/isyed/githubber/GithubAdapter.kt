package com.isyed.githubber

import android.app.Activity
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isyed.githubber.GithubUser
import com.isyed.githubber.MyApplication
import com.isyed.githubber.R
import com.squareup.picasso.Picasso


class GithubAdapter(
    private var githubUsers: List<GithubUser> = mutableListOf(),
    val githubUserClickedCallback: (githubUser : GithubUser)-> Unit)
                        : RecyclerView.Adapter<GithubUserViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        //parent is the view holder
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val myView : View =  inflater.inflate(R.layout.github_user_layout, parent, false)


        return GithubUserViewHolder(myView)


    }

    override fun getItemCount(): Int {
        return githubUsers.size
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.onBind(githubUsers[position], githubUserClickedCallback)
    }

    fun setData(dataSet  : List<GithubUser>){
        this.githubUsers = dataSet
        notifyDataSetChanged()
    }

}