package com.navigation.latihan.storyappsubmission1intermediateandroid.data.response

data class ResponseLoginStory (
    val loginResult: LoginStory,
    val error: Boolean,
    val message: String,
        )

data class LoginStory(

    val name: String,
    val userId: String,
    val token: String,

    )