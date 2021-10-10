package com.example.nasa.adapter.commentsection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.R
import com.example.nasa.databinding.ItemCommentInputBinding

class CommentInputViewHolder(
    view: View,
    val onCommentPost: (comment: String) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCommentInputBinding.bind(itemView)

    fun bind() {
        binding.btnSendComment.setOnClickListener {
            if (binding.tvCommentTextInput.editText?.text.toString().isEmpty()) {
                binding.tvCommentTextInput.error = "You cannot post empty comment"
                binding.tvCommentTextInput.requestFocus()
            } else {
                onCommentPost(binding.tvCommentTextInput.editText?.text.toString())
            }
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onCommentPost: (comment: String) -> Unit
        ): CommentInputViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment_input, parent, false)

            return CommentInputViewHolder(view, onCommentPost)
        }
    }

}