package com.example.rickymortymza.utils

import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharacterResponse
import com.example.rickymortymza.data.Episode
import com.example.rickymortymza.data.EpisodeResponse
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
    suspend fun getAllEpisodes(@Query("page") page: Int): Response<EpisodeResponse>

    @GET("episode")
    suspend fun findEpisodeByName(@Query("name") name: String): Response<EpisodeResponse>

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): Response<Episode>
}