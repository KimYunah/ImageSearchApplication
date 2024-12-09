package com.kya.imagesearchapplication.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kya.imagesearchapplication.R
import com.kya.imagesearchapplication.databinding.ActivitySearchBinding
import com.kya.imagesearchapplication.ui.adapter.ImageListAdapter
import com.kya.imagesearchapplication.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: ImageListAdapter

    /**
     * 검색 DELAY 시간ㅍㅍ
     */
    private val TIME_DELAY_SEARCH: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTitleLayout()
        initRecyclerView()
        observeData()
    }

    /**
     * title layout 설정
     */
    private fun initTitleLayout() {
        // 이전 버튼 이벤트 설정
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 검색 입력 필드 이벤트 설정
        binding.editText.addTextChangedListener(object : TextWatcher {
            private var searchJob: Job? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()

                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchJob = lifecycleScope.launch {
                        delay(TIME_DELAY_SEARCH) // 1초 대기
                        viewModel.searchImages(query)
                    }
                }
            }
        })
    }

    /**
     * recyclerView 설정
     */
    private fun initRecyclerView() {
        adapter = ImageListAdapter(onClick = { item ->
            item.link?.let {
                ViewerActivity.startActivity(this, it)
            }
        }, onBookmarkClick = null)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.searchResults.observe(this) { results ->
            if (results.isEmpty()) {
                Toast.makeText(this, getString(R.string.search_result_empty), Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(results)
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, getString(R.string.search_result_error), Toast.LENGTH_SHORT).show()
        }
    }
}