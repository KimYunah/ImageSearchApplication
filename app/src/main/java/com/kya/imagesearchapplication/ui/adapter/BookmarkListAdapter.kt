package com.kya.imagesearchapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kya.imagesearchapplication.databinding.ItemBookmarkBinding
import com.kya.imagesearchapplication.framework.data.BookmarkEntity

class BookmarkListAdapter(
    private val onClick: (BookmarkEntity) -> Unit
) : ListAdapter<BookmarkEntity, BookmarkListAdapter.ItemBookmarkViewHolder>(DiffCallback()) {

    private var currentState = 0
    private val selectedItems = mutableSetOf<BookmarkEntity>()  // 선택된 아이템

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBookmarkViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemBookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemBookmarkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * 상태 설정
     */
    fun setState(state: Int) {
        currentState = state
    }

    /**
     * 전체 선택 설정
     */
    fun selectAll(isSelected: Boolean) {
        if (isSelected) {
            selectedItems.clear()
            selectedItems.addAll(currentList)
        } else {
            selectedItems.clear()
        }
    }

    /**
     * 선택된 데이터 요청
     */
    fun getSelectedItems(): List<BookmarkEntity> {
        return selectedItems.toList()
    }

    inner class ItemBookmarkViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: BookmarkEntity) {
            if (currentState == 0) {
                binding.ivCheck.visibility = View.GONE
            } else {
                binding.ivCheck.visibility = View.VISIBLE
                binding.ivCheck.isSelected = selectedItems.contains(data)
            }

            // Thumbnail 이미지 설정
            Glide.with(binding.imageThumbnail.context)
                .load(data.thumbnail)
                .into(binding.imageThumbnail)

            // Title 설정
            binding.textTitle.text = data.title

            // 리스트 아이템 클릭
            itemView.setOnClickListener {
                if (currentState == 1) {
                    if (!selectedItems.contains(data)) {
                        selectedItems.add(data)

                        binding.ivCheck.isSelected = true
                    } else {
                        selectedItems.remove(data)

                        binding.ivCheck.isSelected = false
                    }
                }
                onClick(data)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BookmarkEntity>() {
        override fun areItemsTheSame(oldItem: BookmarkEntity, newItem: BookmarkEntity): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: BookmarkEntity, newItem: BookmarkEntity): Boolean =
            oldItem == newItem
    }
}