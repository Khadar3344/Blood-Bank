package com.example.bloodbank.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodbank.R
import com.example.bloodbank.adapters.BloodRequestAdapter
import com.example.bloodbank.databinding.FragmentHomeBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.CustomUser


class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    /*private lateinit var bloodRequestAdapter: BloodRequestAdapter
    private lateinit var customUserArrayList: ArrayList<CustomUser>*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getBloodRequestListFromFireStore()
    }



    private fun getBloodRequestListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getBloodRequestList(this@HomeFragment)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun successBloodRequestListFromStore(bloodRequestList: java.util.ArrayList<CustomUser>) {
        hideProgressDialog()
        if (bloodRequestList.size > 0) {
            binding.recyclerposts.visibility = View.VISIBLE

            binding.recyclerposts.layoutManager = LinearLayoutManager(activity)
            binding.recyclerposts.setHasFixedSize(true)

            val adapterBloodRequest =
                BloodRequestAdapter(this,bloodRequestList)
            binding.recyclerposts.adapter = adapterBloodRequest
            adapterBloodRequest.notifyDataSetChanged()
        } else {
            binding.recyclerposts.visibility = View.GONE
        }
    }



}