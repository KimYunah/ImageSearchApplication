package com.kya.imagesearchapplication.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kya.imagesearchapplication.framework.ImageRepository
import com.kya.imagesearchapplication.framework.data.BookmarkEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: ImageRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _bookmarks = MutableLiveData<List<BookmarkEntity>?>()
    val bookmarks: LiveData<List<BookmarkEntity>?> = _bookmarks

    /**
     * DB 데이터 요청
     */
    fun getBookmarks() {
        viewModelScope.launch {
            try {
                val response = repository.getAllBookmarks()

                _bookmarks.value = response
            } catch (e: Exception) {
                // 에러 처리
                Log.e("BookmarkViewModel", "Error fetching images", e)
            }
        }
    }

    /**
     * DB 데이터 전체 삭제
     */
    fun deleteAllBookmarks() {
        viewModelScope.launch {
            repository.removeAllBookmark()

            getBookmarks()
        }
    }

    /**
     * DB 데이터 삭제
     */
    fun deleteBookmarks(list: List<BookmarkEntity>) {
        viewModelScope.launch {
            list.forEach { repository.removeBookmark(it) }

            getBookmarks()
        }
    }
}