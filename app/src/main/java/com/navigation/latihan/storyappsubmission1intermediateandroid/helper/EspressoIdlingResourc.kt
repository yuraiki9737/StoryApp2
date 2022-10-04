package com.navigation.latihan.storyappsubmission1intermediateandroid.helper

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResourc {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countIdlingResource.increment()
    }

    fun decrement(){
        if(!countIdlingResource.isIdleNow){
            countIdlingResource.decrement()
        }
    }
}

inline fun<T> wrapEspressoIdlingResource(function: () -> T): T{

    EspressoIdlingResourc.increment()
    return try{
        function()
    }finally {
        EspressoIdlingResourc.decrement()
    }
}