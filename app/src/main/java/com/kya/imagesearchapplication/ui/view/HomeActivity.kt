package com.kya.imagesearchapplication.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.kya.imagesearchapplication.R
import com.kya.imagesearchapplication.databinding.ActivityHomeBinding
import com.kya.imagesearchapplication.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    /**
     * UI 설정
     */
    private fun initUI() {
        // Title 영역
        binding.btnSearch.setOnClickListener {
            // SearchActivity 이동
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // Fragment
        val navController = findNavController(R.id.fragment_nav_host)

        // Bottom 영역
        when (navController.currentDestination?.id) {
            R.id.mainFragment -> {
                binding.layoutBottomHome.isSelected = true
                binding.layoutBottomBookmark.isSelected = false
            }
            R.id.bookmarkFragment -> {
                binding.layoutBottomHome.isSelected = false
                binding.layoutBottomBookmark.isSelected = true
            }
        }

        binding.layoutBottomHome.setOnClickListener {
            if (it.isSelected) {
                return@setOnClickListener
            }

            navController.navigate(R.id.mainFragment)

            binding.layoutBottomHome.isSelected = true
            binding.layoutBottomBookmark.isSelected = false
        }

        binding.layoutBottomBookmark.setOnClickListener {
            if (it.isSelected) {
                return@setOnClickListener
            }

            navController.navigate(R.id.bookmarkFragment)

            binding.layoutBottomHome.isSelected = false
            binding.layoutBottomBookmark.isSelected = true
        }
    }
}