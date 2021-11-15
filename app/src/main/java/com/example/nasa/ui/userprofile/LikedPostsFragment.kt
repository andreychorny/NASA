package com.example.nasa.ui.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nasa.adapter.profile.LikedPostsAdapter
import com.example.nasa.databinding.FragmentLikedPostsBinding
import com.example.nasa.navigator
import com.example.nasa.viewmodel.SharedProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikedPostsFragment: Fragment() {

    private lateinit var binding: FragmentLikedPostsBinding

    private val sharedViewModel by viewModels<SharedProfileViewModel>({ requireParentFragment() })

    private var adapter: LikedPostsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikedPostsBinding.inflate(inflater, container, false)
        adapter = LikedPostsAdapter(onGoToDetails)
        binding.rvLikedPosts.adapter = adapter
        sharedViewModel.userActivities().observe(viewLifecycleOwner){
            adapter?.submitList(it.likedPosts?.values?.toList())
        }
        return binding.root
    }

    private val onGoToDetails = {nasaId: String ->
        navigator().goToDetailsPage(nasaId)
    }

    companion object {
        fun newInstance(): LikedPostsFragment {
            return LikedPostsFragment()
        }
    }
}