package com.example.rickymortymza.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickymortymza.adapters.CharacterAdapter
import com.example.rickymortymza.databinding.ActivityMainBinding
import com.example.rickymortymza.utils.managerservice.CharacterRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var characterAdapter: CharacterAdapter
    private val characterRepository = CharacterRepository()
    private val characterList = mutableListOf<com.example.rickymortymza.data.Character>() //Fue la solucion que consegui ya que kotlin me daba error por la palabra character en java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchCharacters()
        }

    private fun setupRecyclerView() {
        // Inicializa el adaptador, pasándole la lista vacía y una funcion de click
        characterAdapter = CharacterAdapter(characterList) { character ->

            Toast.makeText(this, "click en: ${character.name}", Toast.LENGTH_SHORT).show()
        }
        //Se configura el recyclerview
            binding.recyclerViewCharacters.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = characterAdapter
        }
    }

    private fun fetchCharacters() {
        // Usa lifecycleScope.launch para iniciar una corrutina de forma segura
        lifecycleScope.launch {
            try {
                // Llama a tu repositorio para obtener los personajes de la página 1
                val response = characterRepository.getAllCharacter(1) // Obtiene la primera página

                if (response.isSuccessful && response.body() != null) {
                    val characters = response.body()!!.results
                    characterAdapter.updateCharacters(characters)
                } else {
                    Toast.makeText(this@MainActivity, "Error al cargar personajes: ${response.message()}", Toast.LENGTH_LONG).show()
                    Log.e("MainActivity", "Error API: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Excepción: ${e.message}", e)
            }
        }
    }
}