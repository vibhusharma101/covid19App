package com.aossie.covidapp.network

import com.aossie.covidapp.util.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

        val response = call.invoke()

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()

             val message = StringBuilder()

            error?.let {
                try {

                        message.append(JSONObject(it).getString("message")+"jj")

                }
                catch(e:JSONException)
                {


                }
                message.append("\n")


            }

            message.append("ErrorCode : ${response.code()}")
            throw ApiException(message.toString())


        }


    }
}