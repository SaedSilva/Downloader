package br.dev.saed.silverdownloader.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        val retrofit = Retrofit
            .Builder()
            .baseUrl("http://10.0.2.2:8090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}