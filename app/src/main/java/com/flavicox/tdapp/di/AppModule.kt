package com.flavicox.tdapp.di

import com.flavicox.tdapp.data.ImageRepositoryImpl
import com.flavicox.tdapp.repository.ImageRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideImageRepository(
        db: FirebaseFirestore
    ): ImageRepository = ImageRepositoryImpl(
        db = db
    )
}