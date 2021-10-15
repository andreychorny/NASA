package com.example.nasa.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.firebase.NASAPost
import com.example.nasa.R
import com.example.nasa.databinding.ItemNasaImageBinding

class NASAPostViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemNasaImageBinding.bind(itemView)

    fun bind(item: NASAPost) {
        Glide.with(binding.nasaImage.context)
            .load(item.imgUrl)
            .into(binding.nasaImage)
    }

    companion object {
        fun from(parent: ViewGroup): NASAPostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_nasa_image, parent, false)

            return NASAPostViewHolder(view)
        }
    }
}