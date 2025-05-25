package com.example.rickymortymza.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rickymortymza.databinding.ActivityCharacterDetailBinding
import com.squareup.picasso.Picasso

class CharacterDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val characterId = intent.getIntExtra("character_id", -1)
        val  characterName = intent.getStringExtra("character_name")
        val  characterStatus = intent.getStringExtra("character_status")
        val  characterSpecies = intent.getStringExtra("character_species")
        val  characterGender = intent.getStringExtra("character_gender")
        val  characterImage = intent.getStringExtra("character_image")

        binding.detailTextViewName.text = characterName
        binding.detailTextViewStatusSpecies.text = "$characterStatus - $characterSpecies"
        binding.detailTextViewGender.text = characterGender

        //Cargar imagenes con picasso
        characterImage?.let {
            Picasso.get()
                .load(it)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(binding.detailImageViewCharacter)
        }

        supportActionBar?.title = characterName
    }

}