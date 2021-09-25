package com.vanced.manager.ext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T : Any> Call<T>.enqueue(
    onResponse: (call: Call<T>, response: Response<T>) -> Unit,
    onFailure: (call: Call<T>, t: Throwable) -> Unit
) {
    enqueue(object : Callback<T> {

        override fun onResponse(
            call: Call<T>,
            response: Response<T>
        ) = onResponse(call, response)

        override fun onFailure(
            call: Call<T>,
            t: Throwable
        ) = onFailure(call, t)

    })
}