package com.example.nasa.viewstate

import android.graphics.Bitmap

sealed class ProfileViewState {

    object Loading: ProfileViewState()

    object LoadingProfilePhoto: ProfileViewState()

    data class ProfileImgLoaded(val bitmap: Bitmap): ProfileViewState()

    object ProfileDefaultImg: ProfileViewState()

    data class ProfileImgUploadSuccess(val bitmap: Bitmap): ProfileViewState()

    data class Error(val message: String): ProfileViewState()


}