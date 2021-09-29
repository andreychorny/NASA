package com.example.nasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.domain.models.NASAImageModel
import com.example.nasa.databinding.FragmentNasaDetailsBinding

class NASADetailsFragment: Fragment() {

    private lateinit var binding: FragmentNasaDetailsBinding

    private lateinit var nasaModel: NASAImageModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nasaModel = savedInstanceState?.getParcelable(MODEL) ?:
                arguments?.getParcelable(MODEL) ?:
                throw IllegalArgumentException("You need to specify model to launch this fragment")

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNasaDetailsBinding.inflate(inflater, container, false)
        Glide.with(binding.nasaDetailImage.context)
            .load(nasaModel.imageUrl)
            .into(binding.nasaDetailImage)
        binding.tvDetailTitle.text = nasaModel.title
        binding.tvDate.text = nasaModel.dateCreated
        binding.tvDescription.text = nasaModel.description
        return binding.root
    }

    companion object {
        @JvmStatic private val MODEL = "NASA_MODEL"

        fun newInstance(model: NASAImageModel): NASADetailsFragment {
            val args = Bundle()
            args.putParcelable(MODEL, model)
            val fragment = NASADetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}