package com.example.bloodbank.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bloodbank.R
import com.example.bloodbank.databinding.FragmentBloodInfoBinding

class BloodInfo : BaseFragment() {

    private lateinit var binding: FragmentBloodInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBloodInfoBinding.inflate(inflater)
        activity?.title = resources.getString(R.string.information)
        return binding.root

    }

}