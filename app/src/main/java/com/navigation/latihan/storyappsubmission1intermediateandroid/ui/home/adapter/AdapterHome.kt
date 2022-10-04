package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.home.adapter

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
import com.navigation.latihan.storyappsubmission1intermediateandroid.R
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryApp
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.detailstory.DetailStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.StoryAppItemBinding

class AdapterHome : PagingDataAdapter<StoryApp, AdapterHome.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryAppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataStory = getItem(position)
        if (dataStory != null) {
            holder.bind(dataStory)
        }
    }

    inner class ViewHolder(private var binding: StoryAppItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryApp) {
            with(binding) {
                Glide.with(photo)
                    .load(story.photoUrl) // URL Avatar
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_broken_image)
                    .into(photo)
                namePerson.text = story.name
                description.text = story.description


                // image OnClickListener
                photo.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(photo, "image"),
                            Pair(namePerson, "name"),
                            Pair(description, "description")
                        )

                    val intent = Intent(it.context, DetailStory::class.java)
                    intent.putExtra(DetailStory.STORY, story)
                    it.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryApp>() {
            override fun areItemsTheSame(
                oldItem: StoryApp,
                newItem: StoryApp
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryApp,
                newItem: StoryApp
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}