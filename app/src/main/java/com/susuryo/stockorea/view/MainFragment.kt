package com.susuryo.stockorea.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.susuryo.stockorea.databinding.FragmentMainBinding
import com.susuryo.stockorea.viewmodel.EnterpriceViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    lateinit var viewModel: EnterpriceViewModel
    private val starAdapter = EnterpricesAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[EnterpriceViewModel::class.java]

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                requestEnterprices()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                binding.no.visibility = View.GONE
                return false
            }
        })

        observeViewModel()
        return binding.root
    }

    private fun requestEnterprices() {
        viewModel.refresh(binding.searchView.query.toString())
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = starAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.stockPrices.observe(viewLifecycleOwner) { countries ->
            countries?.let {
                binding.recyclerview.visibility = View.VISIBLE
                starAdapter.setList(it)
            }
        }

        viewModel.countryLoadError.observe(viewLifecycleOwner) { isError ->
            isError?.let { binding.no.visibility = if (it) View.VISIBLE else View.GONE }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.progressbar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.no.visibility = View.GONE
                    binding.recyclerview.visibility = View.GONE
                }
            }
        }
    }

}