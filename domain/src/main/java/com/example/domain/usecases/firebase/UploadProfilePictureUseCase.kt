package com.example.domain.usecases.firebase

import android.graphics.Bitmap
import com.example.domain.repositories.FirebaseUserRepository
import com.example.domain.usecases.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class UploadProfilePictureUseCase @Inject constructor(private val repo: FirebaseUserRepository):
    SingleUseCase<Bitmap, Bitmap> {

    override fun execute(input: Bitmap): Single<Bitmap> {
        return  repo.uploadProfilePicture(bitmap = input)
    }
}