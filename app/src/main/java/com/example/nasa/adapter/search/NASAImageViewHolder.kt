package com.example.nasa.adapter.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.backend.NASAImageModel
import com.example.nasa.R
import com.example.nasa.databinding.ItemNasaImageBinding

class NASAImageViewHolder(view: View, val onItemClicked : (model: NASAImageModel) -> Unit): RecyclerView.ViewHolder(view) {

    private val binding = ItemNasaImageBinding.bind(itemView)

    fun bind(item: NASAImageModel) {
        Glide.with(binding.nasaImage.context)
            .load(item.imageUrl)
            .into(binding.nasaImage)
        binding.nasaImage.setOnClickListener{
            onItemClicked(item)
        }
    }

    companion object {
        fun from(parent: ViewGroup, onItemClicked : (model: NASAImageModel) -> Unit): NASAImageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_nasa_image, parent, false)

            return NASAImageViewHolder(view, onItemClicked)
        }
    }
}