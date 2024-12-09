package com.kya.imagesearchapplication.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kya.imagesearchapplication.R
import com.kya.imagesearchapplication.databinding.FragmentMainBinding
import com.kya.imagesearchapplication.ui.adapter.ImageListAdapter
import com.kya.imagesearchapplication.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: ImageListAdapter

    /**
     * API 요청 DEFALT 데이터
     */
    private val DEFAULT_SEARCH_QUERY = "만화"
    private val DEFAULT_SEARCH_DISPLAY = 50
    private val DEFAULT_SEARCH_START = 1

    private var currentStart = DEFAULT_SEARCH_START // 현재 페이지 시작 번호

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        initRefreshLayout()
        initRecyclerView()
        observeData()

        refreshImages()

        return binding.root
    }

    /**
     * refresh layout 설정
     */
    private fun initRefreshLayout() {
        binding.layoutSwipeRefresh.setOnRefreshListener {
            refreshImages()
            binding.layoutSwipeRefresh.isRefreshing = false
        }
    }

    /**
     * recyclerView 설정
     */
    private fun initRecyclerView() {
        adapter = ImageListAdapter(onClick = {  item ->
            item.link?.let {
                ViewerActivity.startActivity(requireContext(), it)  // 뷰어 화면 이동
            }
        }, onBookmarkClick = {  item ->
            viewModel.toggleBookmark(item)  // 북마크 설정 변경
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // 스크롤 이벤트 설정
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                val isLoading = viewModel.isLoading.value ?: false
                if (!isLoading && dy > 0 && // 아래로 스크롤 중인지 확인
                        (visibleItemCount + firstVisibleItemPosition >= totalItemCount) &&
                        firstVisibleItemPosition >= 0) {
                    loadMoreImages()
                }
            }
        })
    }

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.images.observe(viewLifecycleOwner) { list ->
            if (currentStart == DEFAULT_SEARCH_START) { // 새로고침
                adapter.submitList(list)
            } else {    // 더보기
                val currentList = adapter.currentList.toMutableList()
                list?.let { currentList.addAll(it) }

                adapter.submitList(currentList)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 새로고침
     */
    private fun refreshImages() {
        currentStart = DEFAULT_SEARCH_START // 페이지 번호 초기화
        viewModel.requestImages(DEFAULT_SEARCH_QUERY, DEFAULT_SEARCH_DISPLAY, currentStart)
    }

    /**
     * 더보기
     */
    private fun loadMoreImages() {
        currentStart += DEFAULT_SEARCH_DISPLAY // 다음 페이지 시작 번호 설정
        viewModel.requestImages(DEFAULT_SEARCH_QUERY, DEFAULT_SEARCH_DISPLAY, currentStart)
    }
}