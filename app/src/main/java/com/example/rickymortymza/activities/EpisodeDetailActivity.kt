package com.example.rickymortymza.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickymortymza.adapters.CharacterAdapter
import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharacterDao
import com.example.rickymortymza.data.CharactersEpisodesDao
import com.example.rickymortymza.data.Episode
import com.example.rickymortymza.data.EpisodeDao
import com.example.rickymortymza.databinding.ActivityEpisodeDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEpisodeDetailBinding
    private lateinit var episodeDao: EpisodeDao
    private lateinit var charactersEpisodesDao: CharactersEpisodesDao
    private lateinit var characterDao: CharacterDao

    private lateinit var charactersInEpisodeAdapter: CharacterAdapter

    lateinit var episode: Episode
    lateinit var characters: List<Character>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEpisodeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Iniciar DAOs
        episodeDao = EpisodeDao(this)
        charactersEpisodesDao = CharactersEpisodesDao(this)
        characterDao = CharacterDao(this)

        charactersInEpisodeAdapter = CharacterAdapter(listOf()) { character ->
            val intent = Intent(this, CharacterDetailActivity::class.java).apply {
                putExtra("selected_character_id", character.id)
            }
            startActivity(intent)
        }
        binding.recyclerViewEpisodeCharacters.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewEpisodeCharacters.adapter = charactersInEpisodeAdapter

        val episodeId = intent.getLongExtra("selected_episode_id", -1L)
        if (episodeId != -1L) {
            episode = episodeDao.findById(episodeId)!!
            loadEpisodeDetails()
            characters = characterDao.findAllByEpisodeId(episodeId)
            charactersInEpisodeAdapter = CharacterAdapter(characters) { character ->
                val intent = Intent(this, CharacterDetailActivity::class.java).apply {
                    putExtra("selected_character_id", character.id)
                }
                startActivity(intent)
            }
            binding.recyclerViewEpisodeCharacters.adapter = charactersInEpisodeAdapter
            binding.recyclerViewEpisodeCharacters.layoutManager = LinearLayoutManager(this)
        } else {
            Log.e("EpisodeDetailActivity", "No episode ID provided. Finishing activity.")
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadEpisodeDetails() {

        binding.detailEpisodeName.text = episode.name
        binding.detailEpisodeCode.text = episode.episode
        binding.detailAirDate.text = episode.air_date
        supportActionBar?.title = episode.name
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}