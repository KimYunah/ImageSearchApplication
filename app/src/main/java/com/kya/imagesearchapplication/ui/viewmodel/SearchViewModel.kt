package com.kya.imagesearchapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kya.imagesearchapplication.R
import com.kya.imagesearchapplication.domain.ImageItem
import com.kya.imagesearchapplication.framework.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ImageRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _searchResults = MutableLiveData<List<ImageItem>>()
    val searchResults: LiveData<List<ImageItem>> = _searchResults

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * 캐시된 데이터 중 검색
     */
    fun searchImages(query: String) {
        viewModelScope.launch {
            try {
                val results = repository.searchByTitle(query)
                _searchResults.value = results
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}