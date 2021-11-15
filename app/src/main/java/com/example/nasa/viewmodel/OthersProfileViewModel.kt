package com.example.nasa.viewmodel

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecases.firebase.SubscribeToUserUseCase
import com.example.domain.usecases.firebase.UnsubscribeFromUserUseCase
import com.example.nasa.rx.SchedulersProvider
import com.example.nasa.viewstate.ProfileViewState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OthersProfileViewModel @Inject constructor(
    private val subscribeToUserUseCase: SubscribeToUserUseCase,
    private val unsubscribeFromUserUseCase: UnsubscribeFromUserUseCase,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val profileState = MutableLiveData<ProfileViewState>()

    fun profileState(): LiveData<ProfileViewState> = profileState

    private val storage = Firebase.storage.reference

    fun loadProfilePicture(username: String) {
        //TODO consider whether pass this logic to repository
        profileState.value = ProfileViewState.LoadingProfilePhoto
        val imgLink = storage.child("users/${username}.jpg")
        imgLink.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val profilePhoto = BitmapFactory.decodeByteArray(it, 0, it.size)
            profileState.value = ProfileViewState.ProfileImgLoaded(profilePhoto)
        }.addOnFailureListener {
            profileState.value = ProfileViewState.ProfileDefaultImg
        }
    }

    fun subscribeToUser(username: String){
        subscribeToUserUseCase.execute(username)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({

            }, {

            })
    }

    fun unsubscribeFromUser(username: String){
        unsubscribeFromUserUseCase.execute(username)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({

            }, {

            })

    }
}