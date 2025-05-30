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
import com.example.rickymortymza.data.CharactersEpisodes
import com.example.rickymortymza.data.CharactersEpisodesDao
import com.example.rickymortymza.data.Episode
import com.example.rickymortymza.data.EpisodeDao
import com.example.rickymortymza.databinding.ActivitySyncBinding
import com.example.rickymortymza.utils.CharacterRepository
import com.example.rickymortymza.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class SyncActivity : AppCompatActivity() {

    lateinit var binding: ActivitySyncBinding

    private var characterList: MutableList<Character> = mutableListOf()
    private var episodesList: MutableList<Episode> = mutableListOf()

    private lateinit var characterRepository: CharacterRepository
    private lateinit var session: SessionManager

    private lateinit var characterDao: CharacterDao
    private lateinit var episodeDao: EpisodeDao
    private lateinit var charactersEpisodesDao: CharactersEpisodesDao

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
        characterRepository = CharacterRepository()

        //iniciamos los daos
        characterDao = CharacterDao(this)
        episodeDao = EpisodeDao(this)
        charactersEpisodesDao = CharactersEpisodesDao(this)

        if (session.getLastDownload() > Calendar.getInstance().timeInMillis - TIME_BETWEEN_DOWNLOAD) {
            Log.d("SyncActivity", "Last download was recent. Skipping sync.")
            goToHome()
            return
        } else {
            Log.d("SynActivity", "realizando una sincronización completa de datos")
            downloadCharacters()
        }
    }

    fun downloadCharacters(page: Int = 1) {
        if (page == 1) characterList.clear()

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
                        Log.d("SynActivity", "Se descargaron todos los personajes. Total: ${characterList.size}")
                        saveCharactersToDatabase()
                    }

                    Log.d("MainActivity", "Número de personajes encontrados: ${characterList.size}")
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.progressTextView.text = "Error al buscar personajes: ${response.code()}"
                        Log.e("MainActivity", "Error al buscar personajes: ${response.code()} - ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainActivity", "Excepción en searchCharacters: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    binding.progressTextView.text = "error de red al descargar personajes ${e.message}"

                }
            }
        }
    }

    fun saveCharactersToDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            characterDao.deleteAll()

            for (index in characterList.indices) {
                val character = characterList[index]
                characterDao.insert(character)
                withContext(Dispatchers.Main) {
                    binding.progressTextView.text = "Guardando personajes (${index + 1}/${characterList.size})"
                }
            }
            Log.d("SyncActivity", "Characters saved to database.")
            downloadEpisodes()
        }
    }

    fun downloadEpisodes(page: Int = 1) {

        if (page == 1) episodesList.clear()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = characterRepository.getAllEpisodes(page)
                if (response.isSuccessful && response.body() != null) {
                    //characterList = response.body()!!.results
                    val result = response.body()!!
                    episodesList.addAll(result.results)

                    withContext(Dispatchers.Main){
                        binding.progressIndicator.max = result.info.pages
                        binding.progressIndicator.progress = page
                        binding.progressTextView.text = "Obteniendo episodios (${episodesList.size}/${result.info.count})"
                    }

                    if (result.info.next != null) {
                        downloadEpisodes(page + 1)
                    } else {
                        Log.d("MainActivity", "Número de episodios encontrados: ${episodesList.size}")
                        saveEpisodesToDatabase()
                    }
                } else {
                    withContext(Dispatchers.Main){
                        binding.progressTextView.text = "error al descargar episodios: ${response.code()}"
                        Log.e("MainActivity", "Error al buscar episodios: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainActivity", "Excepción en searchEpisodes: ${e.message}")
                withContext(Dispatchers.Main) {
                    binding.progressTextView.text = "error de la red al descargar episodios: ${e.message}"

                }
            }
        }
    }

  private fun saveEpisodesToDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            episodeDao.deleteAll()

            for (index in episodesList.indices) {
                val episode = episodesList[index]
                episodeDao.insert(episode)
                withContext(Dispatchers.Main) {
                    binding.progressTextView.text = "Guardando episodios (${index + 1}/${episodesList.size})"
                }
            }
            Log.d("SyncActivity", "Episodes saved to database.")
            saveCharacterEpisodeRelations()
            }
        }

    private fun saveCharacterEpisodeRelations() {
        CoroutineScope(Dispatchers.IO).launch {
            charactersEpisodesDao.deleteAll()
            var relationsCount = 0
            for (character in characterList) {
                for (episodeUrl in character.episode) {
                    val episodeId = extractIdFromUrl(episodeUrl)
                    if (episodeId != null) {
                        val charactersEpisodes = CharactersEpisodes(character.id, episodeId)
                        charactersEpisodesDao.insert(charactersEpisodes)
                        relationsCount++
                    }
                }
            }
            Log.d("SyncActivity", "Character-Episode relations saved: $relationsCount")
            goToHome()
        }
    }


    private fun extractIdFromUrl(url: String): Long? {
        return try {
            url.substringAfterLast('/').toLong()
        } catch (e: Exception) {
            Log.e("SyncActivity", "Error extracting ID from URL: $url", e)
            null
        }
    }

private fun goToHome() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
    }
}
