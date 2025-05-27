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
import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharacterDao
import com.example.rickymortymza.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var characterAdapter: CharacterAdapter
    private var characterList: List<Character> = listOf()


    private lateinit var characterDao: CharacterDao

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


       characterAdapter = CharacterAdapter(characterList) { character ->
           val intent = Intent(this, CharacterDetailActivity::class.java).apply {
               putExtra("character_id", character.id)
               putExtra("character_name", character.name)
               putExtra("character_status", character.status)
               putExtra("character_species", character.species)
               putExtra("character_gender", character.gender)
               putExtra("character_image", character.image)
           }
           startActivity(intent)
       }
        binding.recyclerViewCharacters.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCharacters.adapter = characterAdapter

        searchCharacters("")
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
                searchCharacters(newText)
                return true
            }
        })
        return true
    }

    fun searchCharacters(query: String) {
        characterList = characterDao.findAllByName(query)
        characterAdapter.updateItems(characterList)
    }
}
