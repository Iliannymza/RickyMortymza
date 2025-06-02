package com.example.rickymortymza.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickymortymza.data.Character
import com.example.rickymortymza.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso

class CharacterAdapter(
    var items: List<Character>,
    val onItemClick: (character: Character) -> Unit
) : RecyclerView.Adapter<CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size // Retorna el tamaño de la lista de personajes
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = items[position]
        holder.render(character)

        holder.itemView.setOnClickListener {
            onItemClick(character)
        }
    }

    fun updateItems(items: List<Character>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class CharacterViewHolder(val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {


    fun render(character: Character) {

        binding.textViewName.text = character.name
        binding.textViewStatusSpecies.text = "${character.status} - ${character.species}"
        binding.textViewGender.text = character.gender

        // Cargar imagenes usando Picasso
        Picasso.get()
            .load(character.image)
            .placeholder(android.R.drawable.sym_def_app_icon)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(binding.imageViewCharacter)
        }
    }

