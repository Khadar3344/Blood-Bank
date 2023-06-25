package com.example.bloodbank.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodbank.databinding.PatientListItemBinding
import com.example.bloodbank.models.User
import com.example.bloodbank.ui.activities.ViewPatientsActivity

class PatientAdapter(
    private val context: ViewPatientsActivity,
    private val userList: ArrayList<User>
    ) : RecyclerView.Adapter<PatientAdapter.PatientAdapterViewHolder>() {



    inner class PatientAdapterViewHolder(val binding: PatientListItemBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientAdapterViewHolder {
        return PatientAdapterViewHolder(
            PatientListItemBinding.inflate(
                LayoutInflater.from(parent.context),parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: PatientAdapterViewHolder,
        position: Int
    ) {

        val patient = userList[position]

        holder.binding.apply {
            patientName.text = patient.fullName
            contactNumberPatient.text = patient.mobile
            locationPatient.text = "${patient.address}, ${patient.state}"
            bloodTypeText.text = patient.bloodGroup
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}
