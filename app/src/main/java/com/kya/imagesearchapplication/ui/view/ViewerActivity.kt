package com.kya.imagesearchapplication.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kya.imagesearchapplication.databinding.ActivityViewerBinding
import com.kya.imagesearchapplication.ui.adapter.ViewerListAdapter
import com.kya.imagesearchapplication.ui.viewmodel.ViewerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewerActivity : AppCompatActivity() {
    private val viewModel: ViewerViewModel by viewModels()
    private lateinit var binding: ActivityViewerBinding
    private lateinit var adapter: ViewerListAdapter

    /**
     * COUNT DEFALT 데이터
     */
    private val DEFAULT_IMAGES_COUNT = 30

    private lateinit var clickedImage: String

    companion object {
        const val EXTRA_IMAGE_LINK = "extra_image_link"

        fun startActivity(context: Context, imageLink: String) {
            val intent = Intent(context, ViewerActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_LINK, imageLink)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTitleLayout()
        initRecyclerView()
        observeData()

        clickedImage = intent.getStringExtra(EXTRA_IMAGE_LINK) ?: ""

        viewModel.loadImages(clickedImage, DEFAULT_IMAGES_COUNT)
    }

    /**
     * title layout 설정
     */
    private fun initTitleLayout() {
        // 이전 버튼 이벤트 설정
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    /**
     * recyclerView 설정
     */
    private fun initRecyclerView() {
        adapter = ViewerListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.imageLinks.observe(this) { images ->
            adapter.submitList(images)

            // 클릭된 이미지 위치로 이동
            binding.recyclerView.postDelayed({
                val position = images.indexOfFirst { it == clickedImage }
                if (position >= 0) {
                    (binding.recyclerView.layoutManager as LinearLayoutManager)
                        .scrollToPositionWithOffset(position, 0)
                }
            }, 500)
        }
    }
}