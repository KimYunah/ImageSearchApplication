package com.kya.imagesearchapplication.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kya.imagesearchapplication.R
import com.kya.imagesearchapplication.databinding.FragmentBookmarkBinding
import com.kya.imagesearchapplication.ui.adapter.BookmarkListAdapter
import com.kya.imagesearchapplication.ui.viewmodel.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: BookmarkViewModel by viewModels()
    private lateinit var adapter: BookmarkListAdapter

    /**
     * 화면 상태
     */
    private val STATE_DEFAULT = 0
    private val STATE_EDIT = 1

    private var currentState = STATE_DEFAULT    // 현재 상태

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        initTopLayout()
        initRecyclerView()
        observeData()

        viewModel.getBookmarks()

        return binding.root
    }

    /**
     * top layout 설정
     */
    private fun initTopLayout() {
        binding.tvSelectCount.text = getString(R.string.edit_tv_select_count, 0)

        // 전체 선택 버튼 이벤트 설정
        binding.btnAll.setOnClickListener {
            val newState = !it.isSelected
            it.isSelected = newState

            adapter.selectAll(newState)
            adapter.notifyDataSetChanged()

            if (newState) {
                binding.tvSelectCount.text = getString(R.string.edit_tv_select_count, adapter.currentList.size)
            } else {
                binding.tvSelectCount.text = getString(R.string.edit_tv_select_count, 0)
            }
        }

        // 편집 버튼 이벤트 설정
        binding.btnEdit.setOnClickListener {
            currentState = STATE_EDIT
            changeState()

            adapter.selectAll(false)
        }

        // 삭제 버튼 이벤트 설정
        binding.btnDelete.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            if (adapter.currentList.size == selectedItems.size) {
                viewModel.deleteAllBookmarks()
            } else {
                viewModel.deleteBookmarks(selectedItems)
            }

            currentState = STATE_DEFAULT
            changeState()
        }

        // 취소 버튼 이벤트 설정
        binding.btnCancel.setOnClickListener {
            currentState = STATE_DEFAULT
            changeState()
        }
    }

    /**
     * recyclerView 설정
     */
    private fun initRecyclerView() {
        adapter = BookmarkListAdapter(onClick = {  item ->
            if (currentState == STATE_DEFAULT) {
                item.link.let {
                    ViewerActivity.startActivity(requireContext(), it)
                }
            } else {
                val list = adapter.getSelectedItems()
                binding.btnAll.isSelected = (adapter.currentList.size == list.size)
                binding.tvSelectCount.text = getString(R.string.edit_tv_select_count, list.size)
            }
        })
        adapter.setState(currentState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.bookmarks.observe(viewLifecycleOwner) { items ->
            binding.tvAllCount.text = getString(R.string.edit_tv_all_count, items?.size ?: 0)

            adapter.submitList(items)
        }
    }

    /**
     * 상태 변경
     */
    private fun changeState() {
        if (currentState == STATE_DEFAULT) {
            binding.btnAll.visibility = View.GONE
            binding.tvSelectCount.visibility = View.GONE
            binding.dividerSelectTotal.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
            binding.dividerBtn.visibility = View.GONE
            binding.btnCancel.visibility = View.GONE

            binding.btnEdit.visibility = View.VISIBLE
        } else {
            binding.btnEdit.visibility = View.GONE

            binding.btnAll.visibility = View.VISIBLE
            binding.tvSelectCount.visibility = View.VISIBLE
            binding.dividerSelectTotal.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE
            binding.dividerBtn.visibility = View.VISIBLE
            binding.btnCancel.visibility = View.VISIBLE

            binding.btnAll.isSelected = false
            binding.tvSelectCount.text = getString(R.string.edit_tv_select_count, 0)
        }

        adapter.setState(currentState)
        adapter.notifyDataSetChanged()
    }
}