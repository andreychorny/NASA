package com.example.nasa.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.domain.models.backend.NASAImageModel
import com.example.nasa.adapter.profile.ProfilePageAdapter
import com.example.nasa.databinding.FragmentProfilePageBinding
import com.example.nasa.viewmodel.SharedProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class ProfilePageFragment : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding

    private val sharedViewModel by viewModels<SharedProfileViewModel>()

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments?.getString(USERNAME)!!
        sharedViewModel.loadUserActivities(username)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        binding.profileViewPager.adapter = ProfilePageAdapter(username ,childFragmentManager)
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.cancelAllDisposables()
    }

    companion object {

        @JvmStatic
        private val USERNAME = "USERNAME"

        fun newInstance(username: String): ProfilePageFragment {
            val args = Bundle()
            val fragment = ProfilePageFragment()
            args.putString(USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }
}