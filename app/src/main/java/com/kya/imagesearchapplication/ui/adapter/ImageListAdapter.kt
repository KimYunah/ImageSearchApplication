package com.kya.imagesearchapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kya.imagesearchapplication.databinding.ItemMainBinding
import com.kya.imagesearchapplication.domain.ImageItem

class ImageListAdapter(
    private val onClick: (ImageItem) -> Unit,
    private val onBookmarkClick: ((ImageItem) -> Unit)?
) : ListAdapter<ImageItem, ImageListAdapter.ItemImageViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemImageViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ImageItem) {
            // Thumbnail 이미지 설정
            Glide.with(binding.imageThumbnail.context)
                .load(data.thumbnail)
                .into(binding.imageThumbnail)

            // Title 설정
            binding.textTitle.text = data.title

            // 북마크 설정
            if (onBookmarkClick == null) {
                binding.btnBookmark.visibility = View.GONE
            } else {
                binding.btnBookmark.visibility = View.VISIBLE
                binding.btnBookmark.isSelected = data.isBookmark
                binding.btnBookmark.setOnClickListener {
                    // 버튼 상태 변경
                    val newState = !it.isSelected
                    it.isSelected = newState

                    // 클릭 이벤트 콜백 호출
                    onBookmarkClick.invoke(data.copy(isBookmark = newState))
                }
            }

            // 리스트 아이템 클릭
            itemView.setOnClickListener {
                onClick(data)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean =
            oldItem == newItem
    }
}