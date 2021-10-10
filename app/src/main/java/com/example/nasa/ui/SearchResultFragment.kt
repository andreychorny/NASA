package com.example.nasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nasa.adapter.SearchAdapter
import com.example.nasa.databinding.FragmentSearchResultBinding
import com.example.nasa.viewmodel.SearchResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.domain.models.backend.NASAImageModel
import com.example.nasa.navigator
import com.example.nasa.viewstate.SearchResultViewState


@AndroidEntryPoint
class SearchResultFragment: Fragment() {

    private lateinit var binding: FragmentSearchResultBinding

    private val viewModel by viewModels<SearchResultViewModel>()

    private lateinit var searchQuery: String

    private var adapter: SearchAdapter? = null

    private val onItemClicked = {model: NASAImageModel ->
        this.navigator().goToDetailsPage(model)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchQuery = savedInstanceState?.getString(QUERY) ?:
                arguments?.getString(QUERY) ?:
                throw IllegalArgumentException("You need to specify search query to launch this fragment")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        initRecyclerView()
        viewModel.searchResult().observe(viewLifecycleOwner, { state ->
            when (state){
                is SearchResultViewState.Loading -> {
                    showLoading()
                }
                is SearchResultViewState.Data -> {
                    showData(state)
                }
                is SearchResultViewState.Error -> {

                }
            }
        })
        viewModel.makeSearch(searchQuery)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.cancelAllDisposables()
    }

    private fun showData(state: SearchResultViewState.Data) {
        binding.loading.isVisible = false
        binding.nasaSearchRecyclerView.isVisible = true
        adapter?.submitList(state.items)
    }

    private fun showLoading() {
        binding.loading.isVisible = true
        binding.nasaSearchRecyclerView.isVisible = false
    }

    private fun initRecyclerView() {
        adapter = SearchAdapter(onItemClicked)
        binding.nasaSearchRecyclerView.adapter = adapter
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        manager.isItemPrefetchEnabled = true
        binding.nasaSearchRecyclerView.layoutManager = manager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY, searchQuery)
    }

    companion object {
        @JvmStatic private val QUERY = "SEARCH_QUERY"

        fun newInstance(query: String): SearchResultFragment {
            val args = Bundle()
            args.putString(QUERY, query)
            val fragment = SearchResultFragment()
            fragment.arguments = args
            return fragment
        }
    }

}