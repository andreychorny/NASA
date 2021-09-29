package com.example.nasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.domain.models.NASAImageModel
import com.example.nasa.databinding.ActivityMainBinding
import com.example.nasa.ui.MainPageFragment
import com.example.nasa.ui.NASADetailsFragment
import com.example.nasa.ui.SearchResultFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, MainPageFragment.newInstance())
            .commit()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun goToMainPage() {
        launchFragment(MainPageFragment.newInstance())
    }

    override fun goToSearchResult(query: String) {
        launchFragment(SearchResultFragment.newInstance(query))
    }

    override fun goToDetailsPage(model: NASAImageModel) {
        launchFragment(NASADetailsFragment.newInstance(model))
    }

}