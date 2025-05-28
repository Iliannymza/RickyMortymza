package com.example.rickymortymza.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rickymortymza.R
import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharacterDao
import com.example.rickymortymza.data.Episode
import com.example.rickymortymza.data.EpisodeDao
import com.example.rickymortymza.databinding.ActivitySyncBinding
import com.example.rickymortymza.utils.CharacterRepository
import com.example.rickymortymza.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class SyncActivity : AppCompatActivity() {

    lateinit var binding: ActivitySyncBinding

    private var characterList: MutableList<Character> = mutableListOf()
    private var episodesList: MutableList<Episode> = mutableListOf()

    private lateinit var characterRepository: CharacterRepository

    private lateinit var session: SessionManager

    private val TIME_BETWEEN_DOWNLOAD = 7 * 24 * 60 * 60 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivitySyncBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        if (session.getLastDownload() > Calendar.getInstance().timeInMillis - TIME_BETWEEN_DOWNLOAD) {
            goToHome()
            return
        }

        characterRepository = CharacterRepository()

        downloadCharacters()
    }

    fun downloadCharacters(page: Int = 1) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = characterRepository.getAllCharacters(page)
                if (response.isSuccessful && response.body() != null) {
                    //characterList = response.body()!!.results
                    val result = response.body()!!
                    characterList.addAll(result.results)

                    CoroutineScope(Dispatchers.Main).launch {
                        binding.progressIndicator.max = result.info.pages
                        binding.progressIndicator.progress = page
                        binding.progressTextView.text = "Obteniendo personajes (${characterList.size}/${result.info.count})"
                    }

                    if (result.info.next != null) {
                        downloadCharacters(page + 1)
                    } else {
                        saveCharactersToDatabase()
                    }

                    Log.d("MainActivity", "Número de personajes encontrados: ${characterList.size}")
                } else {
                    CoroutineScope(Dispatchers.Main).launch {

                    }
                    Log.e("MainActivity", "Error al buscar personajes: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainActivity", "Excepción en searchCharacters: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {

                }
            }
        }
    }

    fun saveCharactersToDatabase() {
        val characterDao = CharacterDao(this)

        characterDao.deleteAll()

        for (index in characterList.indices) {
            binding.progressTextView.text = "Guardando personajes (${index + 1}/${characterList.size})"
            characterDao.insert(characterList[index])
        }

        downloadEpisodes()
    }

    fun downloadEpisodes(page: Int = 1) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = characterRepository.getAllEpisodes(page)
                if (response.isSuccessful && response.body() != null) {
                    //characterList = response.body()!!.results
                    val result = response.body()!!
                    episodesList.addAll(result.results)

                    CoroutineScope(Dispatchers.Main).launch {
                        binding.progressIndicator.max = result.info.pages
                        binding.progressIndicator.progress = page
                        binding.progressTextView.text = "Obteniendo episodios (${episodesList.size}/${result.info.count})"
                    }

                    if (result.info.next != null) {
                        downloadEpisodes(page + 1)
                    } else {
                        saveEpisodesToDatabase()
                    }

                    Log.d("MainActivity", "Número de episodios encontrados: ${episodesList.size}")
                } else {
                    CoroutineScope(Dispatchers.Main).launch {

                    }
                    Log.e("MainActivity", "Error al buscar episodios: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainActivity", "Excepción en searchEpisodes: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {

                }
            }
        }
    }

    fun saveEpisodesToDatabase() {
        val episodeDao = EpisodeDao(this)

        episodeDao.deleteAll()

        for (index in episodesList.indices) {
            binding.progressTextView.text = "Guardando episodios (${index + 1}/${episodesList.size})"
            episodeDao.insert(episodesList[index])
        }

        session.setLastDownload(Calendar.getInstance().timeInMillis)

        goToHome()
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}