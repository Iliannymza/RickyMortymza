package com.example.rickymortymza.utils.managerservice

import com.example.rickymortymza.data.CharacterResponse
import com.example.rickymortymza.data.remote.RickAndMortyApiService
import com.example.rickymortymza.utils.network.RetrofitClient
import retrofit2.Response

class CharacterRepository {
    private val apiService: RickAndMortyApiService = RetrofitClient.instance

    suspend fun getAllCharacter(page: Int): Response<CharacterResponse> {
        return apiService.getAllCharacters(page)
    }

    suspend fun findCharactersByName(name: String): Response<CharacterResponse> {
        return apiService.findCharacterByName(name)
    }

    suspend fun getCharacterById(id: Int): Response<com.example.rickymortymza.data.Character> {
        return apiService.getCharacterById(id)
    }
}