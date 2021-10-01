package com.example.nasa.ui

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.domain.models.User
import com.example.nasa.databinding.FragmentAuthenticationBinding
import com.example.nasa.navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AuthenticationFragment: Fragment() {

    private lateinit var binding: FragmentAuthenticationBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        binding.tvRegister.setOnClickListener{
            showRegisterScreen()
        }
        binding.tvSignIp.setOnClickListener{
            showLoginScreen()
        }
        binding.btnSignIn.setOnClickListener{
            signInUser()
        }
        binding.btnSignUp.setOnClickListener {
            createNewAccount()
        }
        showLoginScreen()
        return binding.root
    }

    private fun signInUser(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    this.navigator().goBack()
                } else {
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createNewAccount(){
        val nickname = binding.etNickname.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val repeatedPassword = binding.etRepeatPassword.text.toString()
        if (validateWrongInput(nickname, email, password, repeatedPassword)) return
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(nickname, email)
                    FirebaseAuth.getInstance().currentUser?.let {
                        createUserEntryInDatabase(it, user)
                    }
                    this.navigator().goBack()
                }else{
                    Toast.makeText(context, "Sign-Up error occurred", Toast.LENGTH_SHORT).show()
                    Log.e("AuthenticationFragment:", task.exception?.message?: "")
                }
            }
    }

    private fun validateWrongInput(
        nickname: String,
        email: String,
        password: String,
        repeatedPassword: String
    ): Boolean {
        if (nickname.length < 6) {
            binding.etNickname.setError("Nickname is too short")
            binding.etNickname.requestFocus()
            return true
        }
        if (!nickname.lowercase().contains(CONTAINS_LETTERS_REGEX)) {
            binding.etNickname.error = "Nickname must contain letters"
            binding.etNickname.requestFocus()
            return true
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please provide valid email"
            binding.etEmail.requestFocus()
            return true
        }
        if (password.length < 6) {
            binding.etPassword.error = "Password should have more than 6 symbols"
            binding.etPassword.requestFocus()
            return true
        }
        if (repeatedPassword != password) {
            binding.etRepeatPassword.error = "Passwords must match"
            binding.etRepeatPassword.requestFocus()
            return true
        }
        return false
    }

    private fun createUserEntryInDatabase(
        it: FirebaseUser,
        user: User
    ) = FirebaseDatabase.getInstance().getReference("users")
        .child(it.uid)
        .setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "New account was created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Sign-Up error occurred", Toast.LENGTH_SHORT).show()
                Log.e("AuthenticationFragment:", it.exception?.message?: "")
            }
        }

    private fun showLoginScreen(){
        binding.btnSignIn.isVisible = true
        binding.btnSignUp.isVisible = false
        binding.etEmail.isVisible = true
        binding.etNickname.isVisible = false
        binding.etPassword.isVisible = true
        binding.etRepeatPassword.isVisible = false
        binding.tvSignIp.isVisible = false
        binding.tvRegister.isVisible = true
    }
    private fun showRegisterScreen(){
        binding.btnSignIn.isVisible = false
        binding.btnSignUp.isVisible = true
        binding.etEmail.isVisible = true
        binding.etNickname.isVisible = true
        binding.etPassword.isVisible = true
        binding.etRepeatPassword.isVisible = true
        binding.tvSignIp.isVisible = true
        binding.tvRegister.isVisible = false
    }
    companion object {

        private val CONTAINS_LETTERS_REGEX = "[a-z]".toRegex()

        fun newInstance(): AuthenticationFragment {
            return AuthenticationFragment()
        }
    }
}