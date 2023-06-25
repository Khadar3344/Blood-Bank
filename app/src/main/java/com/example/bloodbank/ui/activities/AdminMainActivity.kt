package com.example.bloodbank.ui.activities

import android.content.Intent
import android.os.Bundle
import com.example.bloodbank.databinding.ActivityAdminMainBinding

class AdminMainActivity : BaseActivity() {
    lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {

            btnViewPatients.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, ViewPatientsActivity::class.java))
            }

            btnViewDonors.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, ViewDonorsActivity::class.java))
            }

            btnReports.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, ReportsActivity::class.java))
            }

            btnLogout.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, LoginActivity::class.java))
            }
        }

    }

}