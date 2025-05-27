package com.example.rickymortymza.utils

import com.example.rickymortymza.data.CharacterResponse
import com.example.rickymortymza.data.remote.RickAndMortyApiService
import retrofit2.Response

class CharacterRepository {
    private val apiService: RickAndMortyApiService = RetrofitClient.instance

    suspend fun getAllCharacters(page: Int): Response<CharacterResponse> {
        return apiService.getAllCharacters(page)
    }

    suspend fun findCharactersByName(name: String): Response<CharacterResponse> {
        return apiService.findCharacterByName(name)
    }

    suspend fun getCharacterById(id: Int): Response<com.example.rickymortymza.data.Character> {
        return apiService.getCharacterById(id)
    }
}