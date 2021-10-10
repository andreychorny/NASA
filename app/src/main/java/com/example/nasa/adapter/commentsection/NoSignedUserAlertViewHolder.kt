package com.example.nasa.adapter.commentsection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.R
import com.example.nasa.databinding.ItemNoSignedUserAlertBinding

class NoSignedUserAlertViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemNoSignedUserAlertBinding.bind(itemView)

    fun bind() {

    }

    companion object {
        fun from(
            parent: ViewGroup,
        ): NoSignedUserAlertViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_no_signed_user_alert, parent, false)

            return NoSignedUserAlertViewHolder(view)
        }
    }

}