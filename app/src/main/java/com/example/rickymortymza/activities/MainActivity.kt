package com.example.rickymortymza.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.rickymortymza.databinding.ActivityMainBinding
import com.example.rickymortymza.utils.managerservice.CharacterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var characterAdapter: CharacterAdapter
    private var characterList: MutableList<Character> = mutableListOf()

    private lateinit var characterRepository: CharacterRepository

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

        characterRepository = CharacterRepository()

       characterAdapter = CharacterAdapter(characterList) { character ->
           val intent = Intent(this, CharacterDetailActivity::class.java).apply {
               putExtra("character_id", character.apiid)
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

        searchCharacters("a")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val  menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchCharacters(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    searchCharacters("a")
                }
                return false
            }
        })
        return true
    }

    fun searchCharacters(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = characterRepository.findCharactersByName(query)
                if (response.isSuccessful && response.body() != null) {
                    //characterList = response.body()!!.results
                    characterList.clear()
                    characterList.addAll(response.body()!!.results)

                    Log.d("MainActivity", "Número de personajes encontrados: ${characterList.size}")
                } else {
                    characterList.clear()
                    Log.e("MainActivity", "Error al buscar personajes: ${response.code()}")
                }

                CoroutineScope(Dispatchers.Main).launch {
                    characterAdapter.updateItems(characterList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainActivity", "Excepción en searchCharacters: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    characterList = mutableListOf()
                    characterAdapter.updateItems(characterList)
                }
            }
        }
    }
}
