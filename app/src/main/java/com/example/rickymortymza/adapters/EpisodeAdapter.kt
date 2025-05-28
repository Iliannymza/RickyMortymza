package com.example.rickymortymza.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickymortymza.data.Episode
import com.example.rickymortymza.databinding.ItemEpisodeBinding

class EpisodeAdapter(
    var items: List<Episode>,
    val onItemClick: (episode: Episode) -> Unit
) : RecyclerView.Adapter<EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding =
            ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = items[position]
        holder.render(episode)
        holder.itemView.setOnClickListener {
            onItemClick(episode)
        }
    }

    fun updateItems(newItems: List<Episode>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}

class EpisodeViewHolder(val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {
    fun render(episode: Episode) {
        binding.textViewEpisodeName.text = episode.name
        binding.textViewEpisodeCode.text = episode.episode
        binding.textViewAirDate.text = episode.air_date
    }
}
