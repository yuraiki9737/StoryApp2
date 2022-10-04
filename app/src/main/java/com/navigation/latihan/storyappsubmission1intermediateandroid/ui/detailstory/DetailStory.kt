package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.detailstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.navigation.latihan.storyappsubmission1intermediateandroid.R
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.response.StoryApp
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.addstory.AddStory
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.ActivityDetailStoryBinding
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.setting.Setting
import com.navigation.latihan.storyappsubmission1intermediateandroid.ui.viewmodel.DetailViewModel

class DetailStory : AppCompatActivity() {

    private lateinit var binding:ActivityDetailStoryBinding

    private lateinit var storyApp : StoryApp

    private val detailViewModel : DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingButton()
        viewDetailStory()


    }

    private fun bindingButton(){
        binding.btnCreate.setOnClickListener{
            val view = Intent(this@DetailStory, AddStory::class.java)
            startActivity(view)
        }

        binding.btnSetting.setOnClickListener {
            val view = Intent(this@DetailStory, Setting::class.java)
            startActivity(view)

        }
    }

    private fun viewDetailStory(){
        storyApp = intent.getParcelableExtra(STORY)!!
        detailViewModel.detailStory(storyApp)

            binding.namePersonStory.text = detailViewModel.storyAppItem.name
            binding.descriptionStory.text = detailViewModel.storyAppItem.description

            Glide.with(this)
                .load(detailViewModel.storyAppItem.photoUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_broken_image)
                .centerCrop()
                .into(binding.imageViewStory)

    }

    companion object{
        const val STORY = "story"
    }
}