package com.example.nasa.ui

import android.R.attr
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nasa.databinding.FragmentProfileBinding
import com.example.nasa.navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.provider.MediaStore
import android.app.Activity.RESULT_OK
import androidx.activity.result.contract.ActivityResultContracts
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder

import android.os.Build
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.nasa.GlideApp
import com.example.nasa.R
import com.example.nasa.viewmodel.AuthenticationViewModel
import com.example.nasa.viewmodel.ProfileViewModel
import com.example.nasa.viewstate.ProfileViewState
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<ProfileViewModel>()

    private lateinit var auth: FirebaseAuth

    private lateinit var database: DatabaseReference

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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        if(auth.uid == null) this.navigator().goBack()
        database = Firebase.database.reference
        binding.tvNickname.text = Firebase.auth.currentUser?.displayName
        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()
            this.navigator().goBack()
        }
        binding.ivEditImage.setOnClickListener {
            onUploadImage.launch("image/*")
        }
        setViewStatesProcessing()
        viewModel.loadCurrentUser(auth.uid!!)
        return binding.root
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
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}