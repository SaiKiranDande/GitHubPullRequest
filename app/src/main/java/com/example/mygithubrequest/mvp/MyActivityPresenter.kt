package com.example.mygithubrequest.mvp

import android.util.Log
import com.example.mygithubrequest.*
import com.example.mygithubrequest.data.DetailsPref
import com.example.mygithubrequest.data.GitHubDetails
import com.example.mygithubrequest.databinding.ItemGithubListBinding
import com.github.nitrico.lastadapter.LastAdapter
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_github_list.view.*
import org.joda.time.DateTime
import retrofit2.Response
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.UnknownHostException

class MyActivityPresenter constructor(val view: MyActivityView) {

    private val TAG = MyActivityPresenter::class.java.simpleName
    private val compositeDisposable = CompositeDisposable()
    private val apiManager = ApiClient.getClient().create(ApiInterface::class.java)
    private val lastAdapter: LastAdapter by lazy { initLastAdapter() }
    private val listOfData: MutableList<GitHubDetails> = mutableListOf()

    //Api call to get the list response
    private fun getAllPullList() {
        view.handleLoading(true)
        compositeDisposable.add(
            apiManager.getPullList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ getAllPullListResponse(it) }, { onResponseError(it) })
        )
    }

    //Show dialog when the fab is clicked
    internal fun onFabClickEvent() {
        Log.i(TAG, "onFabClickEvent")
        view.showDetailsDialog()
    }

    //setting up an adapter
    internal fun setAdapter() {
        view.setUpAdapter(lastAdapter)
    }


    /**
     * @param response is a list of data class response
     * adding the list and updating the adapter
     * handling the views according to response
     */
    private fun getAllPullListResponse(response: Response<List<GitHubDetails>>) {
        Log.i(TAG, "getAllPullListResponse = " + response.isSuccessful)
        if (response.isSuccessful) {
            Log.i(TAG, "getAllPullListResponse listSize = " + response.body()?.size)
            if (response.body()!!.isNotEmpty()) {
                listOfData.clear()
                listOfData.addAll(response.body()!!)
                lastAdapter.notifyDataSetChanged()
            }
        } else {
            Log.i(TAG, "getAllPullListResponse msg = ${response.message()}, code = ${response.code()} ")
            if (response.code() != 404)
                view.showToastMessage("Something went wrong, please try later")
        }
        view.handleLoading(false)
        view.handleViews(response.body()?.isNotEmpty() ?: false)
        view.dismissDialog()
    }

    /**
     * @param error is an error from Api call response
     * Handling the major error response
     *
     */
    private fun onResponseError(error: Throwable) {
        Log.i(TAG, "onResponseError = $error")
        view.handleLoading(false)
        when (error) {
            is ConnectException -> view.showToastMessage("Please check your connection")
            is UnknownHostException -> view.showToastMessage("Please check your connection")
            is NoRouteToHostException -> view.showToastMessage("Invalid request url")
            else -> view.showToastMessage(error.message ?: "Please try again.")
        }
    }

    //Populating the data
    private fun initLastAdapter() =
        LastAdapter.with(listOfData, BR.githubDetails)
            .map<GitHubDetails, ItemGithubListBinding>(R.layout.item_github_list) {
                onBind {
                    Picasso.with(view.context).load(binding.githubDetails?.user?.avatar_url).into(view.item_user_iv)
                    if (binding.githubDetails?.created_at != null) {
                        val date = DateTime(binding.githubDetails?.created_at).toDate()
                        view.item_date_tv.visible()
                        view.item_date_tv.text = date.formatDate()
                    } else {
                        view.item_date_tv.gone()
                    }
                }
            }

    /**
     * @param name
     * @param repo
     * adding the both string values to KotPref class (shared preference)
     * and calling the gitAllPullList() method
     */
    internal fun getRequest(name: String, repo: String) {
        if (validateData(name, repo)) {
            DetailsPref.ownerName = name
            DetailsPref.repoName = repo
            getAllPullList()
        }
    }

    //validating the data entered
    private fun validateData(name: String, repo: String): Boolean {
        return if (name.isEmpty() || name.isBlank()) {
            view.showToastMessage("Enter owner name")
            false
        } else if (repo.isEmpty() || repo.isBlank()) {
            view.showToastMessage("Enter owner repo")
            false
        } else {
            true
        }
    }

    //Once the screen is destroyed clearing the compositeDisposable
    internal fun clearDisposable() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }


    interface MyActivityView {
        fun showToastMessage(message: String)
        fun showDetailsDialog()
        fun dismissDialog()
        fun setUpAdapter(adapter: LastAdapter)
        fun handleViews(show: Boolean)
        fun handleLoading(show: Boolean)
    }

}