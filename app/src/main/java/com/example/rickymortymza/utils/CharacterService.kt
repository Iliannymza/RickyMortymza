package com.example.rickymortymza.utils

import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharacterResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CharacterService {

    @GET("character")
    suspend fun findCharacterByName(@Query("name") name: String): CharacterResponse

    @GET("{charcter-id}")
    suspend fun findCharacterById(@Path("charcter-id") id: String): Character

    companion object {
        fun getInstance(): CharacterService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(" https://rickandmortyapi.com/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(CharacterService::class.java)
        }
    }
}