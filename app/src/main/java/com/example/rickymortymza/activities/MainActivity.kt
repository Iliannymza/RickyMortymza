package com.example.rickymortymza.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickymortymza.R
import com.example.rickymortymza.adapters.CharacterAdapter
import com.example.rickymortymza.databinding.ActivityMainBinding
import com.example.rickymortymza.utils.managerservice.CharacterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var characterAdapter: CharacterAdapter
    private val characterRepository = CharacterRepository()
    private val characterList = mutableListOf<com.example.rickymortymza.data.Character>() //Fue la solucion que consegui ya que kotlin me daba error por la palabra character en java

    //Variables para las paginas
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false
    private var currentSearchQuery: String? = null
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchCharacters()
        }

    // menu de busqueda
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object  : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Se llama cuando el usuario presiona Enter o el botón de búsqueda
                performSearch(query)
                searchView.clearFocus() // Oculta el teclado
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Se llama cada vez que el texto cambia
                performSearch(newText)
                return true
            }
        })

        searchView?.setOnCloseListener {
            // Se llama cuando el usuario cierra el SearchView
            performSearch(null) // Restablece la búsqueda
            false
        }

        return true
    }

    private fun performSearch(query: String?) {
        searchJob?.cancel() // Cancela cualquier búsqueda anterior para evitar duplicados
        currentSearchQuery = query // Almacena la nueva consulta

        if (query.isNullOrBlank()) {
            // Si la consulta está vacía, reinicia la lista
            currentPage = 1
            isLastPage = false
            characterList.clear() // Limpia la lista existente
            characterAdapter.updateCharacters(characterList) // Notifica al adaptador
            fetchCharacters() // Vuelve a cargar la primera página sin filtro
        } else {
            // Si hay una consulta, inicia la búsqueda con un pequeño retardo (debounce)
            searchJob = lifecycleScope.launch {
                delay(500) // Espera 500ms para que el usuario termine de escribir
                currentPage = 1 // Reinicia la página a 1 para la nueva búsqueda
                isLastPage = false
                characterList.clear() // Limpia la lista existente
                characterAdapter.updateCharacters(characterList) // Notifica al adaptador
                fetchCharacters() // Llama a la API con la consulta de búsqueda
            }
        }
    }

    // --- Fin de métodos para el menú de búsqueda ---

    private fun setupRecyclerView() {
        // Inicializa el adaptador, pasándole la lista vacía y una funcion de click
        characterAdapter = CharacterAdapter(characterList) { character ->

            //Toast.makeText(this, "click en: ${character.name}", Toast.LENGTH_SHORT).show()
            val intent  = Intent(this@MainActivity, CharacterDetailActivity::class.java).apply {
                putExtra("character_id", character.id)
                putExtra("character_name", character.name)
                putExtra("character_status", character.status)
                putExtra("character_species", character.species)
                putExtra("character_gender", character.gender)
                putExtra("character_image", character.image)
            }
            startActivity(intent)

        }
        //Se configura el recyclerview
            binding.recyclerViewCharacters.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = characterAdapter

            // *** Añadimos el OnScrollListener para la paginación ***
            addOnScrollListener(object  : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val  layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val  firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    // Lógica para detectar el final del scroll
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                            firstVisibleItemPosition >= 0 &&
                            totalItemCount >= 20 // Un número arbitrario para empezar a paginar si hay suficientes ítems
                        ) {
                            currentPage++ // Incrementa el número de página
                            fetchCharacters() // Llama a cargar la siguiente página
                        }
                    }
                }
            })
            }
    }

    private fun fetchCharacters() {
        if (isLoading) return
        isLoading = true
        binding.progressBar.visibility = View.VISIBLE

        // Usa lifecycleScope.launch para iniciar una corrutina de forma segura
        lifecycleScope.launch {
            try {
                val response = if (currentSearchQuery.isNullOrBlank()) {
                    characterRepository.getAllCharacters(currentPage)
                } else {
                    characterRepository.findCharactersByName(currentSearchQuery!!)
                }

                if (response.isSuccessful && response.body() != null) {
                    val characters = response.body()!!.results
                    val info = response.body()!!.info

                    if (currentPage == 1 || currentSearchQuery != null) {
                        characterList.clear()
                    }

                    characterList.addAll(characters)
                    characterAdapter.updateCharacters(characterList + characters)

                    isLastPage = info.next == null

                    Log.d("MainActivity", "Número de personajes recibidos: ${characters.size}")
                    if (characters.isNotEmpty()) {
                        val firstChar = characters[0]
                        Log.d("MainActivity", "Primer personaje - Nombre: ${firstChar.name}, Status: ${firstChar.status}, Species: ${firstChar.species}, Image: ${firstChar.image}")
                        if (firstChar.status.isNullOrEmpty() || firstChar.species.isNullOrEmpty()) {
                            Log.w("MainActivity", "El primer personaje tiene status/species vacío o nulo.")
                        }
                        if (firstChar.image.isNullOrEmpty()) {
                            Log.w("MainActivity", "La URL de la imagen del primer personaje está vacía o nula.")
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error al cargar personajes: ${response.message()}", Toast.LENGTH_LONG).show()
                    Log.e("MainActivity", "Error API: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Excepción: ${e.message}", e)
            } finally {
                isLoading = false
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}