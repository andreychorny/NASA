package com.example.nasa.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.firebase.NASAPost
import com.example.nasa.R
import com.example.nasa.databinding.ItemNasaImageBinding

class NASAPostViewHolder(
    view: View,
    private val onGoToDetails: (nasaId: String) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemNasaImageBinding.bind(itemView)

    fun bind(item: NASAPost) {
        Glide.with(binding.nasaImage.context)
            .load(item.imgUrl)
            .into(binding.nasaImage)
        binding.nasaImage.setOnClickListener{ view ->
            item.nasaId?.let { onGoToDetails(it) }
        }
    }

    companion object {
        fun from(parent: ViewGroup, onGoToDetails: (nasaId: String) -> Unit): NASAPostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_nasa_image, parent, false)

            return NASAPostViewHolder(view, onGoToDetails)
        }
    }
}