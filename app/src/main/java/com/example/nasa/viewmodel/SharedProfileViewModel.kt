package com.example.nasa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.firebase.UserActivities
import com.example.domain.usecases.firebase.GetUserActivitiesUseCase
import com.example.nasa.rx.SchedulersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class SharedProfileViewModel @Inject constructor(
    private val getUserActivitiesUseCase: GetUserActivitiesUseCase,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val userActivities = MutableLiveData<UserActivities>()
    fun userActivities(): LiveData<UserActivities> = userActivities
    private val disposables = mutableListOf<Disposable>()

    fun loadUserActivities(nickname: String){
        disposables.add(getUserActivitiesUseCase.execute(nickname)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                userActivities.value = it
            },{

            }))
    }

    fun cancelAllDisposables() {
        disposables.forEach { it.dispose() }
    }
}