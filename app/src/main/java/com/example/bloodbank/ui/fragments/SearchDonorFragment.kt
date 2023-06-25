package com.example.bloodbank.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodbank.R
import com.example.bloodbank.adapters.SearchDonorAdapter
import com.example.bloodbank.databinding.FragmentSearchDonorBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.Donor
import java.util.ArrayList

class SearchDonorFragment : BaseFragment()  {

    private lateinit var binding: FragmentSearchDonorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchDonorBinding.inflate(inflater)
        return binding.root


    }

    override fun onResume() {
        super.onResume()

        binding.apply {

            val bloodGroup = resources.getStringArray(R.array.Blood_Group)
            val arrayBlood = ArrayAdapter(requireContext(), R.layout.blood_group_item, bloodGroup)
            binding.autoCompleteTextViewGetBloodGroup.setAdapter(arrayBlood)

            val state = resources.getStringArray(R.array.state_list)
            val arrayState = ArrayAdapter(requireContext(), R.layout.state_item, state)
            binding.autoCompleteTextViewGetState.setAdapter(arrayState)

            autoCompleteTextViewGetBloodGroup.text.toString().trim() { it <= ' '}
            autoCompleteTextViewGetState.text.toString().trim() { it <= ' '}


            btnSearch.setOnClickListener {
                getDonorListFromFireStore()
            }
        }

    }

    private fun getDonorListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().getSearchDonorList(this@SearchDonorFragment)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun successSearchDonorListFromStore(searchDonorList: ArrayList<Donor>) {
        hideProgressDialog()
            binding.apply {
                if (searchDonorList.size > 0) {

                    showDonorList.visibility = View.VISIBLE

                    showDonorList.layoutManager = LinearLayoutManager(context)
                    showDonorList.setHasFixedSize(true)

                    val adapterSearchDonor =
                        SearchDonorAdapter(this@SearchDonorFragment,searchDonorList)
                    showDonorList.adapter = adapterSearchDonor
                    adapterSearchDonor.notifyDataSetChanged()
            } else { 
                    showDonorList.visibility = View.GONE
                    Toast.makeText(requireContext(), "لا يوجد", Toast.LENGTH_SHORT).show()
            }
        }
    }

}