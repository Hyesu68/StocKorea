package com.susuryo.stockorea.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.susuryo.stockorea.databinding.FragmentStarBinding

class StarFragment : Fragment() {

    private lateinit var binding: FragmentStarBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStarBinding.inflate(inflater, container, false)

        return binding.root
    }
}