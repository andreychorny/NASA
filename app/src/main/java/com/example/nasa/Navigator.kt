package com.example.nasa

import androidx.fragment.app.Fragment
import com.example.domain.models.NASAImageModel

fun Fragment.navigator(): Navigator{
    return requireActivity() as Navigator
}

interface Navigator {

    fun goBack()

    fun goToMainPage()

    fun goToSearchResult(query: String)

    fun goToDetailsPage(model: NASAImageModel)
}