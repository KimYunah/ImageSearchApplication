package com.kya.imagesearchapplication.framework

import com.kya.imagesearchapplication.domain.ImageItem
import com.kya.imagesearchapplication.framework.data.BookmarkEntity
import com.kya.imagesearchapplication.framework.db.BookmarkDao
import com.kya.imagesearchapplication.framework.network.ImageApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val apiService: ImageApiService,
    private val bookmarkDao: BookmarkDao
) {

    private var cachedItems = listOf<ImageItem>() // 메인탭 데이터 캐싱

    /**
     * 서버 데이터 요청
     */
    suspend fun requestImages(query: String, display: Int, start: Int): List<ImageItem>? {
        return withContext(Dispatchers.IO) {
            val serverResponse = apiService.searchImages(query, display, start)  // 서버 응답 데이터
            val allBookmarks = bookmarkDao.getAllBookmarks()    // DB 저장 데이터

            val result = serverResponse.items?.map { item ->
                ImageItem(
                    title = item.title,
                    thumbnail = item.thumbnail,
                    link = item.link,
                    isBookmark = allBookmarks.any { it.title == item.title } // 북마크 여부 설정
                )
            } ?: emptyList()

            if (start > 1) {
                cacheItems(result, true)
            } else {
                cacheItems(result)
            }

            result
        }
    }

    /**
     * DB 데이터 전체 요청
     */
    suspend fun getAllBookmarks(): List<BookmarkEntity>? {
        return bookmarkDao.getAllBookmarks()
    }

    /**
     * DB 데이터 삽입
     */
    suspend fun addBookmark(bookmark: BookmarkEntity) {
        bookmarkDao.insertBookmark(bookmark)

        updateCachedItems(bookmark, isBookmarked = true)
    }

    /**
     * DB 데이터 전체 삭제
     */
    suspend fun removeAllBookmark() {
        bookmarkDao.deleteAllBookmarks()

        // 캐싱 데이터 갱신
        cachedItems = cachedItems.map { item ->
            item.copy(isBookmark = false)
        }
    }

    /**
     * DB 데이터 삭제
     */
    suspend fun removeBookmark(bookmark: BookmarkEntity) {
        bookmarkDao.deleteBookmark(bookmark)

        updateCachedItems(bookmark, isBookmarked = false)
    }

    /**
     *  캐시된 데이터 검색(title 검색)
     */
    suspend fun searchByTitle(query: String): List<ImageItem> {
        val filteredItems = mutableListOf<ImageItem>()
        for (item in cachedItems) {
            val lowerTitle = item.title?.lowercase()
            if (lowerTitle?.contains(query.lowercase()) == true) {
                filteredItems.add(item)
            }
        }

        return filteredItems
    }

    /**
     *  서버 데이터 캐시(메인탭에서 로드된 데이터)
     */
    private fun cacheItems(items: List<ImageItem>, isLoadMore: Boolean = false) {
        if (isLoadMore) {
            cachedItems = mutableListOf<ImageItem>().apply {
                addAll(cachedItems)
                addAll(items)
            }
        } else {
            cachedItems = items
        }
    }

    /**
     * 캐싱 데이터 갱신
     */
    private fun updateCachedItems(bookmark: BookmarkEntity, isBookmarked: Boolean) {
        cachedItems = cachedItems.map { item ->
            if (item.title == bookmark.title) {
                item.copy(isBookmark = isBookmarked)
            } else {
                item
            }
        }
    }

    /**
     *  link 목록 요청
     */
    suspend fun loadImages(clickedLink: String, count: Int): List<String> {
        val clickedIndex = cachedItems.indexOfFirst { it.link == clickedLink }
        if (clickedIndex == -1) {   // 동일한 link가 없는 경우
            return cachedItems.take(count).map { it.link ?: "" }  // 0번째부터 count 개수 반환
        }

        var startIndex = maxOf(0, clickedIndex - count / 2)
        var endIndex = minOf(cachedItems.size, clickedIndex + count / 2)
        if (endIndex - startIndex < count) {
            val remaining = count - (endIndex - startIndex)
            startIndex = maxOf(0, startIndex - remaining)
            endIndex = minOf(cachedItems.size, endIndex + remaining)
        }

        return cachedItems.subList(startIndex, endIndex).map { it.link ?: "" }
    }
}