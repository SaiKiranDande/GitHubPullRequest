package com.example.mygithubrequest.data

import com.chibatching.kotpref.KotprefModel
import java.util.*

data class GitHubDetails(
    val id: Long,
    val title: String,
    val url: String,
    val state: String,
    val user: UserDetails,
    val created_at: Date
)

data class UserDetails(
    val id: Long,
    val login: String,
    val avatar_url: String
)

object DetailsPref : KotprefModel() {

    var ownerName: String by stringPrefVar("")
    var repoName: String by stringPrefVar("")
}
