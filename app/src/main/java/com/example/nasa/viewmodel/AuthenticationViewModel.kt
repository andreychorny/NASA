package com.example.nasa.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.User
import com.example.nasa.viewstate.AuthenticationViewState
import com.example.nasa.viewstate.SearchResultViewState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
): ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val authenticationState = MutableLiveData<AuthenticationViewState>()
    fun authenticationState(): LiveData<AuthenticationViewState> = authenticationState

    fun signInUser(email: String, password: String){
        authenticationState.value = AuthenticationViewState.Loading
        auth.currentUser
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authenticationState.value = AuthenticationViewState.Success
                } else {
                    authenticationState.value = AuthenticationViewState.Error("Authentication failed.")
                    Log.e("AuthenticationFragment", task.exception?.message?: "")
                }
            }
    }

    fun createNewAccount(nickname: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(nickname)
                    FirebaseAuth.getInstance().currentUser?.let {
                        createUserEntryInDatabase(it, user)
                    }
                    val profile = userProfileChangeRequest {
                        displayName = nickname
                    }
                    FirebaseAuth.getInstance().currentUser?.updateProfile(profile)
                    authenticationState.value = AuthenticationViewState.Success
                }else{
                    authenticationState.value = AuthenticationViewState.Error("Sign-Up error occurred")
                    Log.e("AuthenticationFragment", task.exception?.message?: "")
                }
            }
    }

    private fun createUserEntryInDatabase(
        it: FirebaseUser,
        user: User
    ) = FirebaseDatabase.getInstance().getReference("users")
        .child(it.uid)
        .setValue(user).addOnCompleteListener {
            if (!it.isSuccessful){
                authenticationState.value = AuthenticationViewState.Error("Sign-Up error occurred")
                Log.e("AuthenticationFragment", it.exception?.message?: "")
            }
        }
}