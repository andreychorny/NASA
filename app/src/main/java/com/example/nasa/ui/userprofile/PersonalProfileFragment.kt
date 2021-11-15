package com.example.nasa.ui.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nasa.navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.nasa.R
import com.example.nasa.databinding.FragmentPersonalProfileBinding
import com.example.nasa.viewmodel.PersonalProfileViewModel
import com.example.nasa.viewmodel.SharedProfileViewModel
import com.example.nasa.viewstate.ProfileViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalProfileFragment : Fragment() {

    private lateinit var binding: FragmentPersonalProfileBinding

    private val viewModel by viewModels<PersonalProfileViewModel>()

    private val sharedViewModel by viewModels<SharedProfileViewModel>({ requireParentFragment() })

    private lateinit var auth: FirebaseAuth

    private val onUploadImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            var bitmap: Bitmap? = null
//            val contentResolver = requireActivity().contentResolver
//            bitmap = if (Build.VERSION.SDK_INT < 28) {
//                MediaStore.Images.Media.getBitmap(contentResolver, uri)
//            } else {
//                val source: ImageDecoder.Source =
//                    ImageDecoder.createSource(contentResolver, uri)
//                ImageDecoder.decodeBitmap(source)
//            }
            Glide.with(this).asBitmap().load(uri).centerCrop()
                .into(object : SimpleTarget<Bitmap?>(200, 200) {
                override fun onResourceReady( resource: Bitmap,
                                              transition: Transition<in Bitmap?>?) {
                    viewModel.updateProfileImage(resource)
                }
            })
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalProfileBinding.inflate(inflater, container, false)
        if(auth.uid == null) this.navigator().goBack()
        binding.tvNickname.text = Firebase.auth.currentUser?.displayName
        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()
            this.navigator().goBack()
        }
        binding.ivEditImage.setOnClickListener {
            onUploadImage.launch("image/*")
        }
        sharedViewModel.userActivities().observe(viewLifecycleOwner){
            binding.tvAmountOfLikedPosts.text = it.likedPosts?.size.toString()
        }
        setViewStatesProcessing()
        viewModel.loadProfilePicture()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.cancelAllDisposables()
    }

    private fun setViewStatesProcessing() {
        viewModel.profileState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileViewState.ProfileImgUploadSuccess -> {
                    binding.ivProfileImg.setImageBitmap(state.bitmap)
                    Toast.makeText(
                        context,
                        "Profile image updates successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
        fun newInstance(): PersonalProfileFragment {
            return PersonalProfileFragment()
        }
    }
}