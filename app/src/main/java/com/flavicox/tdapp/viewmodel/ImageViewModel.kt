package com.flavicox.tdapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flavicox.tdapp.entity.Response
import com.flavicox.tdapp.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repo: ImageRepository
): ViewModel() {
    var getImageFromDatabaseResponse by mutableStateOf<Response<String>>(Response.Success(null))
    private set

    fun getImageFromDatabase() = viewModelScope.launch {
        getImageFromDatabaseResponse = Response.Loading
        getImageFromDatabaseResponse = repo.getImageFromFirestoreResponse()
    }
}