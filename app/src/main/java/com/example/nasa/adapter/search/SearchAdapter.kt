package com.example.nasa.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.NASAImageModel
import com.example.nasa.adapter.search.NASAImageViewHolder

class SearchAdapter(val onItemClicked : (model: NASAImageModel) -> Unit) : ListAdapter<NASAImageModel, NASAImageViewHolder>((NASADiffUtil())) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NASAImageViewHolder {
        return NASAImageViewHolder.from(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: NASAImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class NASADiffUtil : DiffUtil.ItemCallback<NASAImageModel>() {
    override fun areItemsTheSame(oldItem: NASAImageModel, newItem: NASAImageModel): Boolean {
        return oldItem.nasaId == newItem.nasaId
    }

    override fun areContentsTheSame(oldItem: NASAImageModel, newItem: NASAImageModel): Boolean {
        return oldItem == newItem
    }

}
