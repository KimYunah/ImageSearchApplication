package com.kya.imagesearchapplication.framework.network

import com.kya.imagesearchapplication.framework.data.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApiService {

    @GET("/v1/search/image")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("display") display: Int = 50,
        @Query("start") start: Int = 1
    ): ImageResponse

}