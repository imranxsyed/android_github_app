package com.isyed.githubber

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.isyed.githubber.presenter.MainActivityPresenter
import com.isyed.githubber.view.IMainActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Response



class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, IMainActivity {
    companion object{
        const val EX_GITHUB_USER = "com.isyed.githubber.MainActivity.GITHUB_USER"
    }

    private var presenter : MainActivityPresenter? = null
    //Adapter for recyclerview <code> activity_main_sv_users</code>
    lateinit var githubUsersAdapter : GithubAdapter
    //my progress loader
    private lateinit  var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bind()

        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        activity_main_sv_users.setOnQueryTextListener(this)

        //setup  recyclerview
        //1 adapter
        githubUsersAdapter = GithubAdapter(mutableListOf(), this::githubUserClickedCallback)
        //2 linear layout
        //attach to recyclerview
        activity_main_rv_users.layoutManager = LinearLayoutManager(this)
        activity_main_rv_users.adapter = githubUsersAdapter

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //if empty or became empty
        activity_main_sv_users.clearFocus()
        if (query != null && query.isNotEmpty()){

            progressDialog.setMessage("Retrieving Users ....")
            progressDialog.show()

            getGithubUsers(query)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean { return false }

    override fun getGithubUsers(name : String){
        //check if network is available
        val isNetworkAvailable = MyApplication.isNetworkAvailable()
        if (isNetworkAvailable){
            //MAKE API CALL
            presenter?.getGithubUsers(name)
//            MyApplication.githubApi.getUsers(name).enqueue(githubUsersCallback)

        }
        else{
            Toast.makeText(this, "!!!No Internet Detected!!!", Toast.LENGTH_SHORT).show()
        }
    }

    //The method is called when all the data is ready to be displayed in the list
    //or called when there is no data received from the server
    override  fun postGetGithubUsers(githubUsers: List<GithubUser>){
        //stop progress bar
        githubUsersAdapter.setData(githubUsers)
        progressDialog.dismiss()
    }

    private fun githubUserClickedCallback(githubUser : GithubUser){

        val intent = Intent(this, GitHubUserDetails::class.java)
        intent.putExtra(EX_GITHUB_USER, githubUser)
        startActivity(intent)
    }

    override fun bind() {
        presenter = MainActivityPresenter(this)
    }

    override fun unBind() {
        presenter = null
    }
}