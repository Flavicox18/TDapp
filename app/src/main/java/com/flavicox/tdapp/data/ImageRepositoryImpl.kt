package com.flavicox.tdapp.data

import com.flavicox.tdapp.entity.Response
import com.flavicox.tdapp.repository.GetImageFromFirestoreResponse
import com.flavicox.tdapp.repository.ImageRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import com.flavicox.tdapp.utils.Constants.IMAGES
import com.flavicox.tdapp.utils.Constants.UID
import com.flavicox.tdapp.utils.Constants.URL
import kotlinx.coroutines.tasks.await

class ImageRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): ImageRepository {

    override suspend fun getImageFromFirestoreResponse(): GetImageFromFirestoreResponse {
        return try{
            val imageUrl = db.collection(IMAGES).document(UID).get().await().getString(URL)
            Response.Success(imageUrl)
        }
        catch (e: Exception){
            Response.Failure(e)
        }
    }
}