package com.example.rickymortymza.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickymortymza.R
import com.example.rickymortymza.adapters.CharacterAdapter
import com.example.rickymortymza.adapters.EpisodeAdapter
import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharacterDao
import com.example.rickymortymza.data.Episode
import com.example.rickymortymza.data.EpisodeDao
import com.example.rickymortymza.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var characterAdapter: CharacterAdapter
    private var characterList: List<Character> = listOf()

    private lateinit var episodeAdapter: EpisodeAdapter
    private var episodeList: List<Episode> = listOf()

    private var query = ""

    private lateinit var characterDao: CharacterDao

    private lateinit var episodeDao: EpisodeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        characterDao = CharacterDao(this)
        episodeDao = EpisodeDao(this)



        characterAdapter = CharacterAdapter(characterList) { character ->
            val intent = Intent(this, CharacterDetailActivity::class.java).apply {
                putExtra("selected_character_id", character.id)
            }
            startActivity(intent)
        }
        binding.recyclerViewCharacters.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCharacters.adapter = characterAdapter

        episodeAdapter = EpisodeAdapter(episodeList) { episode ->
            val intent = Intent(this, EpisodeDetailActivity::class.java).apply { // se cambia por episodeDetail
                putExtra("selected_episode_id", episode.id)
            }
            startActivity(intent)
        }

        binding.tabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.tabBar.selectedTabPosition) {
                    0 -> loadCharacters()
                    1 -> loadEpisodes()
                }
                search()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        search()
    }

    private fun loadEpisodes() {
        binding.recyclerViewCharacters.adapter = episodeAdapter
    }

    private fun loadCharacters() {
        binding.recyclerViewCharacters.adapter = characterAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val  menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                search()
                return true
            }
        })
        return true
    }

    fun search() {
        if (binding.tabBar.selectedTabPosition == 0) {
            characterList = characterDao.findAllByName(query)
            characterAdapter.updateItems(characterList)
        } else {
            episodeList = episodeDao.findAllByName(query)
            episodeAdapter.updateItems(episodeList)
        }
    }
}
