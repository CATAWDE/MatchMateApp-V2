package com.example.matchmateapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matchmateapp.databinding.ItemMatchUserBinding
import com.example.matchmateapp.domain.model.MatchStatus
import com.example.matchmateapp.domain.model.MatchUser

class MatchMateAdapter(
    private val onStatusChanged: (MatchUser, MatchStatus) -> Unit
) : PagingDataAdapter<MatchUser, MatchMateAdapter.MatchViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder =
        MatchViewHolder(
            ItemMatchUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    inner class MatchViewHolder(
        private val binding: ItemMatchUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: MatchUser) {
            binding.nameText.text = user.name
            binding.detailsText.text = listOfNotNull(
                user.age?.let { "$it years" },
                user.city,
                user.country
            ).joinToString(" • ")

            binding.statusText.text = user.status.name

            Glide.with(binding.profileImage)
                .load(user.imageUrl)
                .into(binding.profileImage)

            binding.acceptButton.setOnClickListener {
                onStatusChanged(user, MatchStatus.ACCEPTED)
            }

            binding.declineButton.setOnClickListener {
                onStatusChanged(user, MatchStatus.DECLINED)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MatchUser>() {
            override fun areItemsTheSame(oldItem: MatchUser, newItem: MatchUser): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MatchUser, newItem: MatchUser): Boolean =
                oldItem == newItem
        }
    }
}
