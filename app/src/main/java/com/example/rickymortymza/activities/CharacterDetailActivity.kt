package com.example.rickymortymza.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rickymortymza.data.CharacterDao
import com.example.rickymortymza.databinding.ActivityCharacterDetailBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CharacterDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailBinding

    lateinit var characterDao: CharacterDao

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

        val characterId = intent.getIntExtra("character_id", -1)
        val  characterName = intent.getStringExtra("character_name") ?: "Nombre Desconocido"
        val  characterStatus = intent.getStringExtra("character_status") ?: "Desconocido"
        val  characterSpecies = intent.getStringExtra("character_species") ?: "Desconocida"
        val  characterGender = intent.getStringExtra("character_gender") ?: "Desconocido"
        val  characterType = intent.getStringExtra("character_image")
        val  characterOrigin = intent.getStringExtra("character_image")
        val  characterLocation = intent.getStringExtra("character_image")
        val  characterEpisode = intent.getStringExtra("character_image")
        val  characterUrl = intent.getStringExtra("character_image")
        val  characterCreated = intent.getStringExtra("character_image")
        val  characterImage = intent.getStringExtra("character_image")

        binding.detailTextViewName.text = characterName
        binding.detailTextViewStatusSpecies.text = "$characterStatus - $characterSpecies"
        binding.detailTextViewGender.text = characterGender

        //Cargar imagenes con picasso
        characterImage?.let { imageUrl ->
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
                        // Opcional: mostrar un Toast o mensaje de error
                    }
                })
        } ?: run {
            // Si characterImage es nulo, oculta el ProgressBar inmediatamente y no carga imagen
            binding.detailImageProgressBar.visibility = View.GONE
            // Puedes establecer una imagen por defecto si la URL es nula
            binding.detailImageViewCharacter.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // Habilita el botón de retroceso en la Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = characterName // Asegúrate de que characterName no sea null para el título
    }

    // Maneja el clic en el botón de retroceso de la Toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}