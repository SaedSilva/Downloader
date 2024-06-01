package br.dev.saed.silverdownloader.api

import br.dev.saed.silverdownloader.model.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LinkAPI {

    @GET("api/link/code/{code}")
    suspend fun getUrl(@Path("code") code: Int): Response<Url>
}