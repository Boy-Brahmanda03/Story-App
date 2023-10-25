package com.example.storyapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ItemStoryLayoutBinding

class StoriesAdapter(
    val onClick: (ListStoryItem) -> Unit
) :
    ListAdapter<ListStoryItem, StoriesAdapter.StoriesViewHolder>(DIFF_CALLBACK) {

    class StoriesViewHolder(private val binding: ItemStoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvTitle.text = story.name
            binding.tvStoryDesctiption.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivPicture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding =
            ItemStoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
        holder.itemView.setOnClickListener {
            onClick(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}