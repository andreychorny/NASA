package com.example.nasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nasa.adapter.profile.ProfilePageAdapter
import com.example.nasa.databinding.FragmentProfilePageBinding
import com.example.nasa.viewmodel.ProfileViewModel
import com.example.nasa.viewmodel.SharedProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilePageFragment : Fragment() {

    private lateinit var binding: FragmentProfilePageBinding

    private val sharedViewModel by viewModels<SharedProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        binding.profileViewPager.adapter = ProfilePageAdapter(childFragmentManager)
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager)
        Firebase.auth.currentUser?.displayName?.let { sharedViewModel.loadUserActivities(it) }
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.cancelAllDisposables()
    }

    companion object {
        fun newInstance(): ProfilePageFragment {
            return ProfilePageFragment()
        }
    }
}