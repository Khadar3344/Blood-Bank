package com.example.bloodbank.ui.fragments

import android.os.Bundle
import android.text.util.Linkify
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bloodbank.R
import com.example.bloodbank.databinding.FragmentAboutUsBinding

class AboutUs : BaseFragment() {

    private lateinit var binding: FragmentAboutUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater)
        activity?.title = resources.getString(R.string.about_us)
        Linkify.addLinks(binding.txtv, Linkify.ALL)
        return binding.root
    }
}