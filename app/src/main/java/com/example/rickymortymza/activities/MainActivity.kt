package com.example.rickymortymza.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickymortymza.R

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CharacterAdapter

    var characterList: List<Character> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        adapter = CharacterAdapter(characterList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        searchCharacter("a")
    }

    fun searchCharacter(query: String) {
        // Llamada en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = CharacterService.getInstance()
                val response = service.findCharacterByName(query)
                characterList = response.results

                // Volvemos al hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.updateItems(characterList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}