package com.example.storyapp.ui.adapter


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.local.room.entity.StoriesEntity
import com.example.storyapp.databinding.ItemStoryLayoutBinding
import com.example.storyapp.ui.detailstory.DetailStoryActivity

class StoriesAdapter(
    private val userToken: String
) :
    PagingDataAdapter<StoriesEntity, StoriesAdapter.StoriesViewHolder>(DIFF_CALLBACK) {

    class StoriesViewHolder(private val binding: ItemStoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoriesEntity, token: String) {
            binding.tvTitle.text = story.name
            binding.tvStoryDesctiption.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivPicture)

            itemView.setOnClickListener {
                val detailIntent = Intent(itemView.context, DetailStoryActivity::class.java)
                detailIntent.putExtra(DetailStoryActivity.EXTRA_STORY_ID, story.id)
                detailIntent.putExtra(DetailStoryActivity.EXTRA_TOKEN, token)

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.ivPicture, "picture"),
                    Pair(binding.tvTitle, "name"),
                    Pair(binding.tvStoryDesctiption, "description")
                )

                itemView.context.startActivity(detailIntent, activityOptionsCompat.toBundle())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding =
            ItemStoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story, userToken)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoriesEntity>() {
            override fun areItemsTheSame(oldItem: StoriesEntity, newItem: StoriesEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoriesEntity,
                newItem: StoriesEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}