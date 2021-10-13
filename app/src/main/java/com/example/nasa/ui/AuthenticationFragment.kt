package com.example.nasa.ui

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nasa.databinding.FragmentAuthenticationBinding
import com.example.nasa.navigator
import com.example.nasa.viewmodel.AuthenticationViewModel
import com.example.nasa.viewstate.AuthenticationViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment: Fragment() {

    private lateinit var binding: FragmentAuthenticationBinding

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        setupButtons()
        viewModel.authenticationState().observe(viewLifecycleOwner){ state ->
            when (state){
                AuthenticationViewState.Loading -> {

                }
                AuthenticationViewState.Success -> {
                    this.navigator().goBack()
                }
                is AuthenticationViewState.Error ->{
                    Toast.makeText(context, state.message,
                        Toast.LENGTH_SHORT).show()
                }

            }
        }
        showLoginScreen()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.cancelAllDisposables()
    }

    private fun setupButtons() {
        binding.tvRegister.setOnClickListener {
            showRegisterScreen()
        }
        binding.tvSignIp.setOnClickListener {
            showLoginScreen()
        }
        binding.btnSignIn.setOnClickListener {
            viewModel.signInUser(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
        binding.btnSignUp.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val repeatedPassword = binding.etRepeatPassword.text.toString()
            if (validateInput(nickname, email, password, repeatedPassword)) {
                viewModel.createNewAccount(
                    nickname = nickname,
                    email = email,
                    password = password,
                )
            }
        }
    }


    private fun validateInput(
        nickname: String,
        email: String,
        password: String,
        repeatedPassword: String
    ): Boolean {
        if (nickname.length < 6) {
            binding.etNickname.error = "Nickname is too short"
            binding.etNickname.requestFocus()
            return false
        }
        if (!nickname.lowercase().contains(CONTAINS_LETTERS_REGEX)) {
            binding.etNickname.error = "Nickname must contain letters"
            binding.etNickname.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please provide valid email"
            binding.etEmail.requestFocus()
            return false
        }
        if (password.length < 6) {
            binding.etPassword.error = "Password should have more than 6 symbols"
            binding.etPassword.requestFocus()
            return false
        }
        if (repeatedPassword != password) {
            binding.etRepeatPassword.error = "Passwords must match"
            binding.etRepeatPassword.requestFocus()
            return false
        }
        return true
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