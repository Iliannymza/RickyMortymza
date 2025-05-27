package com.example.rickymortymza.utils

import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RickAndMortyApiService {

    @GET("character")
    suspend fun findCharacterByName(@Query("name") name: String): Response<CharacterResponse>

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Response<Character>

    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int): Response<CharacterResponse>

    @GET("episode")
    suspend fun getAllEpisode(@Query("page") page: Int): Response<CharacterResponse>
}