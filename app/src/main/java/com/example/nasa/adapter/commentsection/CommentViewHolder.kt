package com.example.nasa.adapter.commentsection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.GlideApp
import com.example.nasa.R
import com.example.nasa.databinding.ItemCommentBinding
import com.google.firebase.storage.StorageReference

class CommentViewHolder(
    view: View,
    private val storage: StorageReference,
    private val onUserProfileClicked: (username: String) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCommentBinding.bind(itemView)

    fun bind(item: CommentItem) {
        binding.tvSenderNickname.text = item.senderNickname
        binding.tvDatePosted.text = item.dateWritten
        binding.tvCommentText.text = item.text
        binding.clSenderProfile.setOnClickListener{
            onUserProfileClicked(item.senderNickname)
        }
//        GlideApp.with(binding.ivSenderProfileImg).load(Any()).placeholderDrawable
        //TODO That's bad, consider how to improve
        //TODO cache storage reference by Glide
        val profilePath = storage.child("users/${item.senderNickname}.jpg")
        GlideApp.with(binding.ivSenderProfileImg).load(profilePath)
            .placeholder(R.drawable.ic_baseline_person_24).into(binding.ivSenderProfileImg)
    }

    companion object {
        fun from(
            parent: ViewGroup, storage: StorageReference,
            onUserProfileClicked: (username: String) -> Unit
        ): CommentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment, parent, false)

            return CommentViewHolder(view, storage, onUserProfileClicked)
        }
    }

}