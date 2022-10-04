package com.navigation.latihan.storyappsubmission1intermediateandroid.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.navigation.latihan.storyappsubmission1intermediateandroid.api.InterfaceApi
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.RemoteModeratorStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.ResultResponse
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.LoginStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.ResponseRegisterStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryApp
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.roomdb.DbStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.helper.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepo (private val dbStory: DbStory, private val interfaceApi: InterfaceApi){

    fun registerAkun(name: String,
                     email: String,
                     pass: String):
            LiveData<ResultResponse<ResponseRegisterStory>> = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = interfaceApi.registerAkunStoryApp(name, email, pass)
            if (!response.error) {
                emit(ResultResponse.Success(response))
            } else {
                Log.e(TAG, "Register Fail: ${response.message}")
                emit(ResultResponse.Error(response.message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Register Exception: ${e.message.toString()} ")
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun loginAkun(email: String, pass: String): LiveData<ResultResponse<LoginStory>> =
        liveData {
            emit(ResultResponse.Loading)
            try {
                val responseLogin = interfaceApi.loginAkunStoryApp(email, pass)
                if (!responseLogin.error) {
                    emit(ResultResponse.Success(responseLogin.loginResult))
                } else {
                    Log.e(TAG, "Login Fail: ${responseLogin.message}")
                    emit(ResultResponse.Error(responseLogin.message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login Exception: ${e.message.toString()} ")
                emit(ResultResponse.Error(e.message.toString()))
            }
        }

    fun getMap(token: String): LiveData<ResultResponse<List<StoryApp>>> =
        liveData {
            emit(ResultResponse.Loading)
            try {
                val response = interfaceApi.getStoryLocation("Bearer $token")
                if (!response.error) {
                    emit(ResultResponse.Success(response.listStory))
                } else {
                    Log.e(TAG, "GetMap Fail: ${response.message}")
                    emit(ResultResponse.Error(response.message))
                }

            } catch (e: Exception) {
                Log.e(TAG, "GetMap Exception: ${e.message.toString()} ")
                emit(ResultResponse.Error(e.message.toString()))
            }
        }

    fun uploadStory(token: String, description: RequestBody,
        imageMultipart: MultipartBody.Part, lat: RequestBody? = null,
        lon: RequestBody? = null): LiveData<ResultResponse<ResponseRegisterStory>> = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = interfaceApi.imageUploadStory("Bearer $token", description, imageMultipart, lat, lon)
            if (!response.error) {
                emit(ResultResponse.Success(response))
            } else {
                Log.e(TAG, "UploadStory Fail: ${response.message}")
                emit(ResultResponse.Error(response.message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "UploadStory Exception: ${e.message.toString()} ")
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun getPagingStory(token: String): Flow<PagingData<StoryApp>> {
        wrapEspressoIdlingResource {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = 7
                ),
                remoteMediator = RemoteModeratorStory(dbStory, interfaceApi, token),
                pagingSourceFactory = {
                    dbStory.daoStory().getAppStory()
                }
            ).flow
        }
    }


    companion object {
        private const val TAG = "StoryRepository"
    }
}