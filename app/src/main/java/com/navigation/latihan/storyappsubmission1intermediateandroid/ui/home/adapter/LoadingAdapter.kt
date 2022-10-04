package com.navigation.latihan.storyappsubmission1intermediateandroid.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.navigation.latihan.storyappsubmission1intermediateandroid.databinding.LoadingItemBinding

class LoadingAdapter (private val retry: () -> Unit) : LoadStateAdapter<LoadingAdapter.LoadingAdapterViewHolder>() {
    class LoadingAdapterViewHolder (private val binding: LoadingItemBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.tryButton.setOnClickListener {
                    retry.invoke()
                }
            }
        fun bind(state: LoadState){
            if (state is LoadState.Error) {

                binding.progressBar.isVisible = state is LoadState.Loading
                binding.tryButton.isVisible = state is LoadState.Error

            }
        }
    }

    override fun onBindViewHolder(holder: LoadingAdapterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): LoadingAdapterViewHolder {
        val binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingAdapterViewHolder(binding, retry)
    }

}