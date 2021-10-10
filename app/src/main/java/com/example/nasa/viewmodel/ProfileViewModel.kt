package com.example.nasa.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.firebase.User
import com.example.domain.usecases.firebase.GetUserUseCase
import com.example.domain.usecases.firebase.UploadProfilePictureUseCase
import com.example.nasa.rx.SchedulersProvider
import com.example.nasa.viewstate.ProfileViewState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val schedulers: SchedulersProvider
) : ViewModel() {


    private val profileState = MutableLiveData<ProfileViewState>()
    fun profileState(): LiveData<ProfileViewState> = profileState

    private var currentUser: User? = null

    private val storage = Firebase.storage.reference

    private val disposables = mutableListOf<Disposable>()

    fun loadCurrentUser(uid: String) {
        //TODO retrieve friendlist from this
        disposables.add(getUserUseCase.execute(uid)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                currentUser = it
            }, {

            })
        )
        loadProfilePicture()
    }

    private fun loadProfilePicture() {
        //TODO consider whether pass this logic to repository
        profileState.value = ProfileViewState.LoadingProfilePhoto
        val imgLink = storage.child("users/${Firebase.auth.currentUser?.displayName}.jpg")
        imgLink.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val profilePhoto = BitmapFactory.decodeByteArray(it, 0, it.size)
            profileState.value = ProfileViewState.ProfileImgLoaded(profilePhoto)
        }.addOnFailureListener {
            profileState.value = ProfileViewState.ProfileDefaultImg
        }
    }

    fun updateProfileImage(bitmap: Bitmap) {
        profileState.value = ProfileViewState.Loading
        disposables.add(uploadProfilePictureUseCase.execute(bitmap)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                profileState.value = ProfileViewState.ProfileImgUploadSuccess(it)
            }, {
                profileState.value = ProfileViewState.Error(it.message ?: "Image was not updated")
            })
        )
    }

    fun cancelAllDisposables() {
        disposables.forEach { it.dispose() }
    }
}