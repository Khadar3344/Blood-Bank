package com.example.bloodbank.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodbank.R
import com.example.bloodbank.adapters.PatientAdapter
import com.example.bloodbank.databinding.ActivityViewPatientsBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.User

class ViewPatientsActivity : BaseActivity() {
    private lateinit var binding: ActivityViewPatientsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewPatientsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.btn_lbl_view_patients)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getPatientsListFromFirestore()
    }

    private fun getPatientsListFromFirestore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getPatientsList(this)
    }


    fun successPatientsListFromStore(userList: ArrayList<User>) {
        hideProgressDialog()
        binding.apply {
            if (userList.size > 0) {
                recyclerPatients.visibility = View.VISIBLE

                recyclerPatients.layoutManager = LinearLayoutManager(this@ViewPatientsActivity)
                totalPatients.text = userList.size.toString()
                recyclerPatients.setHasFixedSize(true)

                val adapterPatient =
                    PatientAdapter(this@ViewPatientsActivity, userList)
                binding.recyclerPatients.adapter = adapterPatient
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}