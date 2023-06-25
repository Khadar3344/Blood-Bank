package com.example.bloodbank.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodbank.R
import com.example.bloodbank.adapters.DonorAdapter
import com.example.bloodbank.adapters.PatientAdapter
import com.example.bloodbank.databinding.ActivityViewDonorsBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.Donor
import java.util.ArrayList

class ViewDonorsActivity : BaseActivity() {
    private lateinit var binding: ActivityViewDonorsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewDonorsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.btn_lbl_view_donors)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getDonorsListFromFirestore()
    }

    private fun getDonorsListFromFirestore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getDonorsList(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDonorListFromStore(donorList: ArrayList<Donor>) {
        hideProgressDialog()
        binding.apply {
            if (donorList.size > 0) {
                recyclerDonors.visibility = View.VISIBLE

                recyclerDonors.layoutManager = LinearLayoutManager(this@ViewDonorsActivity)
                totalDonors.text = donorList.size.toString()
                recyclerDonors.setHasFixedSize(true)

                val adapterPatient =
                    DonorAdapter(this@ViewDonorsActivity, donorList)
                binding.recyclerDonors.adapter = adapterPatient
            }
        }
    }
}