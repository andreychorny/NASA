package com.example.nasa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecases.backend.GetSearchListUseCase
import com.example.nasa.rx.SchedulersProvider
import com.example.nasa.viewstate.SearchResultViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val searchListUseCase: GetSearchListUseCase,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val searchResult = MutableLiveData<SearchResultViewState>()
    fun searchResult(): LiveData<SearchResultViewState> = searchResult

    private val disposables = CompositeDisposable()

    fun makeSearch(query: String) {
        searchResult.value = SearchResultViewState.Loading
        disposables.add(searchListUseCase.execute(query)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                searchResult.value = SearchResultViewState.Data(it)
            }, {
                searchResult.value = SearchResultViewState.Error("Network error has occurred")
            })
        )
    }

    fun cancelAllDisposables() {
        disposables.dispose()
    }
}