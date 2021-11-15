package com.example.nasa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.payload.SignInRequest
import com.example.domain.payload.SignUpRequest
import com.example.domain.usecases.firebase.authentication.SignInUserUseCase
import com.example.domain.usecases.firebase.authentication.SignUpUserUseCase
import com.example.nasa.rx.SchedulersProvider
import com.example.nasa.viewstate.AuthenticationViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val signInUserUseCase: SignInUserUseCase,
    private val schedulers: SchedulersProvider

) : ViewModel() {

    private val authenticationState = MutableLiveData<AuthenticationViewState>()
    fun authenticationState(): LiveData<AuthenticationViewState> = authenticationState

    private val disposables = CompositeDisposable()

    fun signInUser(email: String, password: String) {
        authenticationState.value = AuthenticationViewState.Loading
        disposables.add(
            signInUserUseCase.execute(SignInRequest(email, password))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe({
                    authenticationState.value = AuthenticationViewState.Success
                }, {
                    authenticationState.value =
                        AuthenticationViewState.Error("Authentication failed.")
                    Log.e("AuthenticationFragment", it.message ?: "")
                })
        )
    }

    fun createNewAccount(nickname: String, email: String, password: String) {
        disposables.add(
            signUpUserUseCase.execute(SignUpRequest(nickname, email, password))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe({
                    authenticationState.value = AuthenticationViewState.Success

                }, {
                    authenticationState.value =
                        AuthenticationViewState.Error("Sign-Up error occurred")

                })
        )

    }

    fun cancelAllDisposables() {
        disposables.dispose()
    }
}