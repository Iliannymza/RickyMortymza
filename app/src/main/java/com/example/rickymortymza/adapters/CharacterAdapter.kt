package com.example.rickymortymza.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rickymortymza.R
import com.example.rickymortymza.data.Character
import com.squareup.picasso.Picasso

class CharacterAdapter(private var characters: List<Character>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.characterNameTextView)
        val imageView: ImageView = itemView.findViewById(R.id.characterImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.nameTextView.text = character.name
        Picasso.get().load(character.image).into(holder.imageView)
        // Aquí podrías cargar la imagen si tu Character tiene una URL de imagen,
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    // *** ESTA ES LA CLAVE DE LA SOLUCIÓN ***
    fun updateItems(newCharacters: List<Character>) {
        this.characters = newCharacters // Reemplaza la lista actual con la nueva
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado por completo
    }
}