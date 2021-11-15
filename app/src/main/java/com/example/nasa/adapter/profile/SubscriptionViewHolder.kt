package com.example.nasa.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.firebase.NASAPost
import com.example.nasa.GlideApp
import com.example.nasa.R
import com.example.nasa.databinding.ItemSubscriptionUserBinding
import com.google.firebase.storage.StorageReference

class SubscriptionViewHolder(
    view: View,
    private val storage: StorageReference
    ) : RecyclerView.ViewHolder(view) {

    private val binding = ItemSubscriptionUserBinding.bind(itemView)

    fun bind(item: String) {
        binding.tvSubscriptionNickname.text = item
        val profilePath = storage.child("users/${item}.jpg")
        GlideApp.with(binding.ivSubscriptionProfileImg).load(profilePath)
            .placeholder(R.drawable.ic_baseline_person_24).into(binding.ivSubscriptionProfileImg)

    }

    companion object {
        fun from(parent: ViewGroup, storage: StorageReference): SubscriptionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subscription_user, parent, false)

            return SubscriptionViewHolder(view, storage)
        }
    }
}