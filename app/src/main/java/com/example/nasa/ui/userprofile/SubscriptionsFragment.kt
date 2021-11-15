package com.example.nasa.ui.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nasa.adapter.profile.LikedPostsAdapter
import com.example.nasa.adapter.profile.SubscriptionsAdapter
import com.example.nasa.databinding.FragmentLikedPostsBinding
import com.example.nasa.databinding.FragmentSubscriptionsBinding
import com.example.nasa.viewmodel.SharedProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionsFragment: Fragment() {

    private lateinit var binding: FragmentSubscriptionsBinding

    private val sharedViewModel by viewModels<SharedProfileViewModel>({ requireParentFragment() })

    private var adapter: SubscriptionsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)
        adapter = SubscriptionsAdapter()
        binding.rvSubscriptions.adapter = adapter
        sharedViewModel.userActivities().observe(viewLifecycleOwner){
            adapter?.submitList(it.subscriptions?.keys?.toList())
        }
        return binding.root
    }

    companion object {
        fun newInstance(): SubscriptionsFragment {
            return SubscriptionsFragment()
        }
    }

}