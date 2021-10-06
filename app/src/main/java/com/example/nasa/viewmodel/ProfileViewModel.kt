package com.example.nasa.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.User
import com.example.nasa.R
import com.example.nasa.viewstate.AuthenticationViewState
import com.example.nasa.viewstate.ProfileViewState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val profileState = MutableLiveData<ProfileViewState>()
    fun profileState(): LiveData<ProfileViewState> = profileState

    private var currentUser: User? = null

    private val database = Firebase.database.reference

    private val storage = Firebase.storage.reference

    fun loadCurrentUser(uid: String){
        //TODO retrieve friendlist from this
        database.child("users").child(uid).get().addOnSuccessListener {
            currentUser = it.getValue<User>()
            loadProfilePicture()
        }
    }

    private fun loadProfilePicture(){
        profileState.value = ProfileViewState.LoadingProfilePhoto
        val imgLink = storage.child("users/${Firebase.auth.currentUser?.displayName}.jpg")
        imgLink.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val profilePhoto = BitmapFactory.decodeByteArray(it, 0, it.size)
            profileState.value = ProfileViewState.ProfileImgLoaded(profilePhoto)
        }.addOnFailureListener {
            profileState.value = ProfileViewState.ProfileDefaultImg
        }

    }

    fun updateProfileImage(bitmap: Bitmap){
        val userImageRef = storage
            .child("users/${auth.currentUser?.displayName}.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        profileState.value = ProfileViewState.Loading
        val uploadTask = userImageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            profileState.value = ProfileViewState.Error(it.message?: "Image was not updated")
        }.addOnSuccessListener {
            profileState.value = ProfileViewState.ProfileImgUploadSuccess(bitmap)
        }
    }
}