package com.isyed.githubber

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    companion object{
        const val EX_GITHUB_USER = "com.isyed.githubber.MainActivity.GITHUB_USER"
    }

    //Adapter for recyclerview <code> activity_main_sv_users</code>
    lateinit var githubUsersAdapter : GithubAdapter
    //layout manager for recyclerview <code> activity_main_sv_users</code>
    lateinit var githubUsersLinearLayout : LinearLayoutManager
    //represents the amount of request sent to fetch the repos for the users. (Usually the same amount as the Github users returned from the server)
    private var requestSent : Int = 0
    //increments for each callback we get for the requests sent
    private var requestRecieved : Int = 0
    //The global variable that is connected with <Code>githubUsersAdapter</Code>
    private var items  = mutableListOf<GithubUser>()
    //my progress loader
    private lateinit  var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this)

        activity_main_sv_users.setOnQueryTextListener(this)

        //setup  recyclerview
        //1 adapter

        githubUsersAdapter = GithubAdapter(items, this::githubUserClickedCallback)
        //1 linear layout
        githubUsersLinearLayout = LinearLayoutManager(this)
        //attach to recyclerview
        activity_main_rv_users.layoutManager = githubUsersLinearLayout
        activity_main_rv_users.adapter = githubUsersAdapter

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //if empty or became empty
        activity_main_sv_users.clearFocus()
        if (query != null && query.isNotEmpty()){

            progressDialog.setMessage("Retrieving Users ....")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.show()
            getGithubUsers(query)


        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean { return false }

    private fun getGithubUsers(name : String){
        //check if network is available
        val isNetworkAvailable = MyApplication.isNetworkAvailable()
        if (isNetworkAvailable){
            //MAKE API CALL
            MyApplication.githubApi.getUsers(name).enqueue(githubUsersCallback)

        }
        else{
            Toast.makeText(this, "!!!No Internet Detected!!!", Toast.LENGTH_SHORT).show()
        }
    }

    //The method is called when all the data is ready to be displayed in the list
    //or called when there is no data received from the server
    private fun done(){
        println(items)
        githubUsersAdapter.setData(items)
        requestRecieved = 0
        requestSent = 0
        //stop progress bar
        progressDialog.dismiss()
    }

    private val githubUsersCallback  = object : retrofit2.Callback<GithubUserResponse>{
        override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
            done()
        }

        override fun onResponse(
            call: Call<GithubUserResponse>,
            response: Response<GithubUserResponse>
        ) {
            if (response.isSuccessful){
                response.body()?.let {
                    requestSent = it.items.size
                    println(requestSent)
                    //need to add repos for each item
                    this@MainActivity.items = it.items
//                    setReposForUsers()
                    done()
                }
            }
        }
    }

    private fun setReposForUsers(){
        //for each item get their repos
        items.forEach {user->
            //making sure it is not null
            user.userRepos = mutableListOf()

            MyApplication.githubApi.getUserRepos(
                Credentials.basic("imranxsyed", "f5f1fb478578a6be5122cebc155a119a902f4c51"),
                user.login
            ).enqueue(
                object : retrofit2.Callback<List<GithubRepo>> {
                    override fun onFailure(
                        call: Call<List<GithubRepo>>,
                        t: Throwable
                    ) {
                        user.userRepos = mutableListOf()
                        requestRecieved += 1
                        println(requestRecieved)
                        if (requestSent == requestRecieved) {
                            done()
                        }

                    }

                    override fun onResponse(
                        call: Call<List<GithubRepo>>,
                        response: Response<List<GithubRepo>>) {
                        requestRecieved += 1
                        println(requestRecieved)
                        println(user.login)
                        if (response.isSuccessful) {
                            response.body()?.let { repos ->
                                Toast.makeText(this@MainActivity,"Response success",Toast.LENGTH_SHORT).show()
                                if (repos.isEmpty()) {
                                    user.userRepos = mutableListOf()
                                } else {
                                    user.userRepos = repos
                                }

                            }
                        }
                        if (requestSent == requestRecieved) {
                            done()
                        }
                    }

                }
            )
        }

        //if there was no item found . call done (Expected to be zero)
        if(requestSent == 0){
            done()
        }
    }

    private fun githubUserClickedCallback(githubUser : GithubUser){

        val intent = Intent(this, GitHubUserDetails::class.java)
        intent.putExtra(EX_GITHUB_USER, githubUser)
        startActivity(intent)
    }
}