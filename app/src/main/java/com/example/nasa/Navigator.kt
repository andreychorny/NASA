package com.example.nasa

import androidx.fragment.app.Fragment
import com.example.domain.models.backend.NASAImageModel

fun Fragment.navigator(): Navigator{
    return requireActivity() as Navigator
}

//TODO refactor navigation by using this:
interface BaseFragmentNavigator<R: Navigator>{
    fun provideNavigator(): R
}

interface Navigator {

    fun goBack()

    fun goToMainPage()

    fun goToSearchResult(query: String)

    fun goToDetailsPage(model: NASAImageModel)

    fun goToDetailsPage(nasaId: String)

    fun goToAuthenticationScreen()

    fun goToProfileScreen(username: String)
}