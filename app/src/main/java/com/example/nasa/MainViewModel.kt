package com.example.nasa

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.usecases.GetSearchListUseCase
import com.example.nasa.rx.SchedulersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val searchListUseCase: GetSearchListUseCase,
    val schedulers: SchedulersProvider
) : ViewModel() {

    fun makeSearch() {
        searchListUseCase.execute()
            .subscribeOn(schedulers.io())
            .subscribe({
                Log.e("!!!!!!!", it.toString())
            }, {
                Log.e("!!!!!!!", it.message?: "error")

            })
    }
}