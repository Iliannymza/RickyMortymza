package com.example.rickymortymza.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickymortymza.data.Character
import com.example.rickymortymza.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso

class CharacterAdapter(
    private val characters: MutableList<Character>, // Lista mutable de personajes que el adaptador mostrará
    private val onItemClick: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    // Se llama para mostrar los datos en una posición específica.
    // Aquí es donde "rellenamos" las vistas con la información del personaje.
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]

        holder.binding.apply { // Usamos apply para acceder a las vistas del binding directamente
            textViewName.text = character.name
            textViewStatusSpecies.text = "${character.status} - ${character.species}"

            // *** Carga la imagen con Picasso ***
            Picasso.get()
                .load(character.image)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(imageViewCharacter)

            root.setOnClickListener {
                onItemClick(character) // Llama a la función lambda que se pasó al adaptador
            }
        }
    }

    // Retorna el número total de elementos en la lista
    override fun getItemCount(): Int = characters.size

    //Metodo para actualizar la lista de personajes y actualizar el recyclerview
    fun updateCharacters(newCharacters: List<Character>) {
        characters.clear() // Limpia la lista existente
        characters.addAll(newCharacters) // Añade los nuevos personajes
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado para que se redibuje
    }
}