package com.example.nasa.adapter.commentsection

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasa.R
import com.example.nasa.databinding.ItemCommentBinding
import com.example.nasa.viewstate.ProfileViewState
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CommentViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemCommentBinding.bind(itemView)

    fun bind(item: CommentItem) {
        binding.tvSenderNickname.text = item.senderNickname
        binding.tvDatePosted.text = item.dateWritten
        binding.tvCommentText.text = item.text
        //TODO That's bad, consider how to improve
        val imgLink = Firebase.storage.reference.child("users/${item.senderNickname}.jpg")
        imgLink.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val profilePhoto = BitmapFactory.decodeByteArray(it, 0, it.size)
            Glide.with(binding.ivSenderProfileImg).load(profilePhoto).into(binding.ivSenderProfileImg)
        }
    }

    companion object {
        fun from(parent: ViewGroup): CommentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment, parent, false)

            return CommentViewHolder(view)
        }
    }

}