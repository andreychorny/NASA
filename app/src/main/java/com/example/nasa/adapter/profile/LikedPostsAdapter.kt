package com.example.nasa.adapter.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.models.firebase.NASAPost

class LikedPostsAdapter(
    private val onGoToDetails: (nasaId: String) -> Unit
) : ListAdapter<NASAPost, NASAPostViewHolder>((NASAPostDiffUtil())) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NASAPostViewHolder {
        return NASAPostViewHolder.from(parent, onGoToDetails)
    }

    override fun onBindViewHolder(holder: NASAPostViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class NASAPostDiffUtil : DiffUtil.ItemCallback<NASAPost>() {
    override fun areItemsTheSame(oldItem: NASAPost, newItem: NASAPost): Boolean {
        return oldItem.nasaId == newItem.nasaId
    }

    override fun areContentsTheSame(oldItem: NASAPost, newItem: NASAPost): Boolean {
        return oldItem == newItem
    }

}