package com.example.bloodbank.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodbank.databinding.DonorListBinding
import com.example.bloodbank.models.Donor
import com.example.bloodbank.ui.activities.ViewDonorsActivity
import java.util.ArrayList

class DonorAdapter(
    private val viewDonorsActivity: ViewDonorsActivity,
    private val donorList: ArrayList<Donor>
) : RecyclerView.Adapter<DonorAdapter.DonorAdapterViewHolder>() {

    inner class DonorAdapterViewHolder(val binding: DonorListBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DonorAdapterViewHolder {
        return DonorAdapterViewHolder(
            DonorListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: DonorAdapterViewHolder,
        position: Int) {

        val donor = donorList[position]

        holder.binding.apply {
            donorName.text = donor.fullName
            contactNumberDonor.text = donor.mobile
            locationDonor.text = "${donor.address}, ${donor.state}"
            bloodTypeDonor.text = donor.bloodGroup
            totalDonation.text = donor.totalDonate.toString()
            lastDonation.text = donor.lastDonate
        }
    }

    override fun getItemCount(): Int {
        return donorList.size
    }

}
