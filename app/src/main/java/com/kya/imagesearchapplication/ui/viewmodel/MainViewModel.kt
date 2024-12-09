package com.kya.imagesearchapplication.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kya.imagesearchapplication.domain.ImageItem
import com.kya.imagesearchapplication.framework.ImageRepository
import com.kya.imagesearchapplication.framework.data.BookmarkEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ImageRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _images = MutableLiveData<List<ImageItem>?>()
    val images: LiveData<List<ImageItem>?> = _images

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /**
     *  서버 데이터 요청
     */
    fun requestImages(query: String, display: Int, start: Int) {
        // 로딩중 여부 확인(중복 호출 방지)
        if (_isLoading.value == true) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = repository.requestImages(query, display, start)
                _images.value = response
            } catch (e: Exception) {    // 에러 처리
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * DB 데이터 추가 / 삭제
     */
    fun toggleBookmark(item: ImageItem) {
        viewModelScope.launch {
            val entity = BookmarkEntity(item.title ?: "", item.thumbnail ?: "", item.link ?: "")

            if (!item.isBookmark) {
                repository.removeBookmark(entity)
            } else {
                repository.addBookmark(entity)
            }
        }
    }
}