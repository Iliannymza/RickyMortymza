package com.example.rickymortymza.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
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
            .inflate(R.layout.item_character, parent, false) // Asegúrate que item_character exista
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.nameTextView.text = character.name
        Picasso.get().load(character.image).into(holder.imageView)
        // Aquí podrías cargar la imagen si tu Character tiene una URL de imagen,
        // usando una librería como Glide o Picasso
        // Ejemplo (requiere librerías de carga de imágenes):
        // Glide.with(holder.imageView.context).load(character.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    // *** ESTA ES LA CLAVE DE LA SOLUCIÓN ***
    fun updateItems(newCharacters: List<Character>) {
        this.characters = newCharacters // Reemplaza la lista actual con la nueva
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado por completo
        // Considera usar DiffUtil.Callback para animaciones más suaves en listas grandes.
        // Pero para una solución rápida, notifyDataSetChanged() es suficiente.
    }
}