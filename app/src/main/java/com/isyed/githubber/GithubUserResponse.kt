package com.isyed.githubber

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUserResponse(
    val total_count : Int,
    val incomplete_results : Boolean,
    val items : MutableList<GithubUser>
) : Parcelable

@Parcelize
data class GithubUser(
    val name : String? = "Jon Doe",
    val login : String,
    val avatar_url : String,
    val followers_url :String,
    val following_url : String,
    val repos_url : String,
    val events_url : String,
    val received_events_url : String,
    var userRepos : List<GithubRepo>? = mutableListOf<GithubRepo>(),
    var email : String?= "N/A",
    var created_at : String?,
    var following : Int,
    val followers : Int,
    val public_repos: Int,
    var bio : String? = "N/A",
    var location : String? = "N/A"
    ):Parcelable

@Parcelize
data class GithubRepo(
    val id : Int,
    val name: String,
    val full_name : String
):Parcelable