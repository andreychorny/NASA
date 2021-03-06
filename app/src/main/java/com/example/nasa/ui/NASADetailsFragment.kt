package com.example.nasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.domain.models.backend.NASAImageModel
import com.example.nasa.R
import com.example.nasa.adapter.commentsection.CommentsSectionAdapter
import com.example.nasa.databinding.FragmentNasaDetailsBinding
import com.example.nasa.navigator
import com.example.nasa.viewmodel.NASADetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class NASADetailsFragment : Fragment() {

    private lateinit var binding: FragmentNasaDetailsBinding

    private val viewModel by viewModels<NASADetailsViewModel>()

    private var adapter: CommentsSectionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nasaModel: NASAImageModel? = savedInstanceState?.getParcelable(MODEL) ?: arguments?.getParcelable(MODEL)
        nasaModel?.let { viewModel.setNasaModel(it) }
        if(nasaModel == null) {
            val nasaId = arguments?.getString(KEY) ?: throw IllegalArgumentException("You must provide " +
                    "either a Nasa model or id to nasa model")
            viewModel.loadNasaPost(nasaId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNasaDetailsBinding.inflate(inflater, container, false)
        initRecyclerView()
        binding.likeButton.setOnClickListener {
            addToLiked()
        }
        viewModel.nasaModel().observe(viewLifecycleOwner){ nasaModel ->
            Glide.with(binding.nasaDetailImage.context)
                .load(nasaModel?.imageUrl)
                .into(binding.nasaDetailImage)
            binding.tvDetailTitle.text = nasaModel?.title
            binding.tvDate.text = nasaModel?.dateCreated
            binding.tvDescription.text = nasaModel?.description

            viewModel.loadComments(nasaModel.nasaId)
            viewModel.loadIsLikedState(nasaModel.nasaId)
        }

        viewModel.nasaComments().observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }
        viewModel.isPostLiked().observe(viewLifecycleOwner){
            when (it){
                null -> binding.likeButton.isVisible = false
                true -> {
                    binding.likeButton.isVisible = true
                    binding.likeButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.heart))
                }
                false -> {
                    binding.likeButton.isVisible = true
                    binding.likeButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.heart_outline))
                }
            }
        }
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = CommentsSectionAdapter(onCommentPost, onUserProfileClicked)
        binding.commentSectionRecyclerView.adapter = adapter

    }

    private val onCommentPost = { comment: String ->
        viewModel.postComment(comment)
    }

    private val onUserProfileClicked = { username: String ->
        navigator().goToProfileScreen(username)
    }

    private fun addToLiked(){
        viewModel.executeLikeButton()
    }

    override fun onStop() {
        super.onStop()
        viewModel.cancelAllDisposables()
    }

    companion object {
        @JvmStatic
        private val MODEL = "NASA_MODEL"
        @JvmStatic
        private val KEY = "NASA_ID"

        fun newInstance(model: NASAImageModel): NASADetailsFragment {
            val args = Bundle()
            args.putParcelable(MODEL, model)
            val fragment = NASADetailsFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(nasaDetailsId: String): NASADetailsFragment {
            val args = Bundle()
            args.putString(KEY, nasaDetailsId)
            val fragment = NASADetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}