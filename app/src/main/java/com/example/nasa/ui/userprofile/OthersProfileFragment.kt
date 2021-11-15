package com.example.nasa.ui.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.domain.models.firebase.UserActivities
import com.example.nasa.R
import com.example.nasa.databinding.FragmentOthersProfileBinding
import com.example.nasa.viewmodel.OthersProfileViewModel
import com.example.nasa.viewmodel.SharedProfileViewModel
import com.example.nasa.viewstate.ProfileViewState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersProfileFragment : Fragment() {

    private lateinit var binding: FragmentOthersProfileBinding

    private val viewModel by viewModels<OthersProfileViewModel>()

    private val sharedViewModel by viewModels<SharedProfileViewModel>({ requireParentFragment() })

    private lateinit var auth: FirebaseAuth

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        username = arguments?.getString(USERNAME)!!
        sharedViewModel.loadUserActivities(username)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOthersProfileBinding.inflate(inflater, container, false)
        binding.tvNickname.text = username
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser == null) binding.btnSubscription.isVisible = false
        sharedViewModel.userActivities().observe(viewLifecycleOwner){
            binding.tvAmountOfLikedPosts.text = it.likedPosts?.size.toString()
            setButton(currentUser, it)
        }
        setViewStatesProcessing()
        viewModel.loadProfilePicture(username)
        return binding.root
    }

    private fun setButton(
        currentUser: FirebaseUser?,
        it: UserActivities
    ) {
        if (currentUser?.displayName != null &&
            it.subscribers?.containsValue(currentUser.displayName) == true
        ) {
            binding.btnSubscription.text = getText(R.string.unsubscribe)
            binding.btnSubscription.setBackgroundColor(resources.getColor(R.color.design_default_color_secondary))
            binding.btnSubscription.setOnClickListener{
                viewModel.unsubscribeFromUser(username)
            }
        } else {
            binding.btnSubscription.text = getText(R.string.subscribe)
            binding.btnSubscription.setBackgroundColor(resources.getColor(R.color.design_default_color_primary_dark))
            binding.btnSubscription.setOnClickListener{
                viewModel.subscribeToUser(username)
            }
        }
    }


    private fun setViewStatesProcessing() {
        viewModel.profileState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileViewState.Loading -> {

                }
                is ProfileViewState.LoadingProfilePhoto -> {

                }
                is ProfileViewState.ProfileImgLoaded -> {
                    binding.ivProfileImg.setImageBitmap(state.bitmap)
                }
                is ProfileViewState.ProfileDefaultImg -> {
                    binding.ivProfileImg.setImageDrawable(context?.getDrawable(R.drawable.ic_baseline_person_24))
                }
                is ProfileViewState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        private val USERNAME = "USERNAME"

        fun newInstance(username: String): OthersProfileFragment {
            val args = Bundle()
            val fragment = OthersProfileFragment()
            args.putString(USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }
}