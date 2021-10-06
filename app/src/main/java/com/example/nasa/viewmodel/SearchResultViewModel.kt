package com.example.nasa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.NASAImageModel
import com.example.domain.usecases.GetSearchListUseCase
import com.example.nasa.rx.SchedulersProvider
import com.example.nasa.viewstate.SearchResultViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val searchListUseCase: GetSearchListUseCase,
    private val schedulers: SchedulersProvider
) : ViewModel(){

    private val searchResult = MutableLiveData<SearchResultViewState>()
    fun searchResult(): LiveData<SearchResultViewState> = searchResult

    fun makeSearch(query: String) {
        searchResult.value = SearchResultViewState.Loading
        //TODO disposable
        searchListUseCase.execute(query)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                searchResult.value = SearchResultViewState.Data(it)
            }, {
                searchResult.value = SearchResultViewState.Error("Network error has occurred")
            })
    }
}