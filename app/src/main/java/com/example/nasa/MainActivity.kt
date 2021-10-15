package com.example.nasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.domain.models.backend.NASAImageModel
import com.example.nasa.databinding.ActivityMainBinding
import com.example.nasa.ui.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    //TODO firebase functions
    //TODO rxjava backpressure
    //TODO rxjava subject
    //TODO workmanager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.materialToolbar.setOnMenuItemClickListener{ menuItem ->
            when(menuItem.itemId){
                R.id.profile ->{
                    goToAuthenticationScreen()
                    true
                }
                else -> false
            }
        }
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

    override fun goToAuthenticationScreen() {
        if(auth.currentUser != null){
            goToProfileScreen()
        }else {
            launchFragment(AuthenticationFragment.newInstance())
        }
    }

    override fun goToProfileScreen() {
        launchFragment(ProfilePageFragment.newInstance())
    }

}