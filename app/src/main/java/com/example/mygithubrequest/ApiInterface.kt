package com.example.mygithubrequest

import com.example.mygithubrequest.data.DetailsPref
import com.example.mygithubrequest.data.GitHubDetails
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    @GET
    fun getPullList(
        @Url url: String = "${DetailsPref.ownerName}/${DetailsPref.repoName}/pulls",
        @HeaderMap headerMap: Map<String, String> = ApiClient.getHeaderMap()
    ): Observable<Response<List<GitHubDetails>>>
}