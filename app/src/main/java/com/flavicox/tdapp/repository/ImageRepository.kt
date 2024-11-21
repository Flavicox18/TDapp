package com.flavicox.tdapp.repository

import com.flavicox.tdapp.entity.Response

typealias GetImageFromFirestoreResponse = Response<String>

interface ImageRepository {
    suspend fun getImageFromFirestoreResponse(): GetImageFromFirestoreResponse
}