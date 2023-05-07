package com.example.devdigitalassignment.core.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devdigitalassignment.R
import com.example.devdigitalassignment.core.ui.viewmodel.MainSharedViewModel
import com.example.devdigitalassignment.databinding.FragmentHomeBinding
import com.kathayat.testWeather.core.domain.model.currentforecast.CurrentWeather
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding?=null
    private val binding by lazy { _binding!! }
    private val mainSharedViewModel: MainSharedViewModel by activityViewModels()
    private lateinit var homeRecylerView:HomeRecylerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.addIconHome.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapsFragment)
        }

        binding.helpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_helpFragment)
        }

        homeRecylerView = HomeRecylerView()
        with( binding.homeRecylerview){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = homeRecylerView
        }
            mainSharedViewModel.allUserData.observe(viewLifecycleOwner, Observer {
                homeRecylerView.differ.submitList(it)
            })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}