package com.example.nasa.adapter.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SubscriptionsAdapter(
) : ListAdapter<String, SubscriptionViewHolder>((SubscriptionDiffUtil())) {

    private val storage = Firebase.storage.reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        return SubscriptionViewHolder.from(parent, storage)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class SubscriptionDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}