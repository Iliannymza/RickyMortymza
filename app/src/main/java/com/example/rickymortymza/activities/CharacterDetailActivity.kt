package com.example.rickymortymza.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickymortymza.adapters.EpisodeAdapter
import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharacterDao
import com.example.rickymortymza.data.CharactersEpisodesDao
import com.example.rickymortymza.data.Episode
import com.example.rickymortymza.data.EpisodeDao
import com.example.rickymortymza.databinding.ActivityCharacterDetailBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CharacterDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailBinding

    lateinit var characterDao: CharacterDao

   lateinit var charactersEpisodesDao: CharactersEpisodesDao

   lateinit var episodeDao: EpisodeDao

   private lateinit var episodesOfCharacterAdapter: EpisodeAdapter

   lateinit var character: Character
   lateinit var episodes: List<Episode>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        characterDao = CharacterDao(this)
        charactersEpisodesDao = CharactersEpisodesDao(this)
        episodeDao = EpisodeDao(this)

        val seletedCharacter = intent.getLongExtra("selected_character_id", -1)

        character = characterDao.findById(seletedCharacter)!!
        episodes = episodeDao.findAllByCharacterId(seletedCharacter)

        episodesOfCharacterAdapter = EpisodeAdapter(episodes){ episode ->
            val intent = Intent(this, EpisodeDetailActivity::class.java).apply { // se cambia por episodeDetail
                putExtra("selected_episode_id", episode.id)
            }
            startActivity(intent)
        }

        binding.recyclerViewCharacterEpisodes.adapter = episodesOfCharacterAdapter
        binding.recyclerViewCharacterEpisodes.layoutManager = LinearLayoutManager(this)

        binding.detailTextViewName.text = character.name
        binding.detailTextViewStatusSpecies.text = "${character.status} - ${character.species}"
        binding.detailTextViewGender.text = character.gender

        //Cargar imagenes con picasso
        character.image?.let { imageUrl ->
            binding.detailImageProgressBar.visibility = View.VISIBLE
            Picasso.get()
                .load(imageUrl)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(binding.detailImageViewCharacter, object : Callback {
                    override fun onSuccess() {
                        binding.detailImageProgressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        binding.detailImageProgressBar.visibility = View.GONE
                    }
                })
        } ?: run {
            binding.detailImageProgressBar.visibility = View.GONE
            binding.detailImageViewCharacter.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = character.name // Asegúrate de que characterName no sea null para el título
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}