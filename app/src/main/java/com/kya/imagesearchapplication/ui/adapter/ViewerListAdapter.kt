package com.kya.imagesearchapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kya.imagesearchapplication.databinding.ItemViewerBinding

class ViewerListAdapter : ListAdapter<String, ViewerListAdapter.ItemViewerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewerViewHolder {
        val binding = ItemViewerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewerViewHolder(private val binding: ItemViewerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            Glide.with(binding.ivImage.context)
                .load(data)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivImage)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}