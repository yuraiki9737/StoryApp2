package com.navigation.latihan.storyappsubmission1intermediateandroid.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginUser (
    val name: String,
    val email: String,
    val userId: String,
    val token: String,
    val isLogin: Boolean

): Parcelable