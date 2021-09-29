package com.example.nasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.nasa.databinding.FragmentMainPageBinding
import com.example.nasa.navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPageFragment: Fragment() {

    private lateinit var binding: FragmentMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater, container, false)
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.isNullOrEmpty()) return false
                goToSearchFragment(query)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        return binding.root
    }

    private fun goToSearchFragment(query:String){
        this.navigator().goToSearchResult(query)
    }

    companion object {
        fun newInstance(): MainPageFragment {
            val fragment = MainPageFragment()
            return fragment
        }
    }
}