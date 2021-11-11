package com.example.nasa.adapter.commentsection

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.backend.NASAImageModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

private const val ITEM_TYPE_COMMENT = 1
private const val ITEM_TYPE_COMMENT_INPUT = 2
private const val ITEM_TYPE_NO_SIGNED_USER_ALERT = 3

class CommentsSectionAdapter(
    private val onCommentPost: (comment: String) -> Unit,
    private val onUserProfileClicked: (username: String) -> Unit
) :
    ListAdapter<CommentsSectionItem, RecyclerView.ViewHolder>(CommentsSectionDiffUtil()) {

    private val storage = Firebase.storage.reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_COMMENT -> CommentViewHolder.from(parent, storage, onUserProfileClicked)
            ITEM_TYPE_COMMENT_INPUT -> CommentInputViewHolder.from(parent, onCommentPost)
            ITEM_TYPE_NO_SIGNED_USER_ALERT -> NoSignedUserAlertViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is CommentItem -> {
                (holder as CommentViewHolder).bind(item)
            }
            is CommentInputItem -> {
                (holder as CommentInputViewHolder).bind()
            }
            is NoSignedUserAlertItem ->{
                (holder as NoSignedUserAlertViewHolder).bind()
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CommentItem -> ITEM_TYPE_COMMENT
            is CommentInputItem -> ITEM_TYPE_COMMENT_INPUT
            is NoSignedUserAlertItem -> ITEM_TYPE_NO_SIGNED_USER_ALERT
        }
    }
}

class CommentsSectionDiffUtil : DiffUtil.ItemCallback<CommentsSectionItem>() {

    override fun areItemsTheSame(
        oldItem: CommentsSectionItem,
        newItem: CommentsSectionItem
    ): Boolean {
        if (oldItem is CommentItem && newItem is CommentItem) {
            return oldItem == newItem
        }
        if (oldItem is CommentInputItem && newItem is CommentInputItem) {
            return true
        }
        if(oldItem is NoSignedUserAlertItem && newItem is NoSignedUserAlertItem){
            return true
        }
        return false
    }

    override fun areContentsTheSame(
        oldItem: CommentsSectionItem,
        newItem: CommentsSectionItem
    ): Boolean {
        if (oldItem is CommentItem && newItem is CommentItem) {
            return oldItem == newItem
        }
        if (oldItem is CommentInputItem && newItem is CommentInputItem) {
            return true
        }
        if(oldItem is NoSignedUserAlertItem && newItem is NoSignedUserAlertItem){
            return true
        }
        return false
    }
}