package com.kya.imagesearchapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kya.imagesearchapplication.framework.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel @Inject constructor(
    private val repository: ImageRepository,
    application: Application
): AndroidViewModel(application) {

    private val _imageLinks = MutableLiveData<List<String>>()
    val imageLinks: LiveData<List<String>> = _imageLinks

    /**
     * 캐시된 데이터 중 이미지 요청
     */
    fun loadImages(clickedImage: String, count: Int) {
        viewModelScope.launch {
            val results = repository.loadImages(clickedImage, count)
            _imageLinks.value = results
        }
    }
}