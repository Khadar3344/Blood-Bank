package com.example.bloodbank.ui.activities

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bloodbank.R
import com.example.bloodbank.databinding.ActivityPostBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.CustomUser
import com.example.bloodbank.models.User
import com.example.bloodbank.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_post.*
import java.util.*

class PostActivity : BaseActivity() {
    private lateinit var binding: ActivityPostBinding
    var Time = ""
    var Date = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPostBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        supportActionBar?.setTitle(R.string.post_blood_request)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bloodGroup = resources.getStringArray(R.array.Blood_Group)
        val arrayBlood = ArrayAdapter(this, R.layout.blood_group_item, bloodGroup)
        binding.autoCompleteTextViewPostBloodGroup.setAdapter(arrayBlood)

        val state = resources.getStringArray(R.array.state_list)
        val arrayState = ArrayAdapter(this, R.layout.state_item, state)
        binding.autoCompleteTextViewPostState.setAdapter(arrayState)

        val cal = Calendar.getInstance()

        var day = cal.get(Calendar.DAY_OF_MONTH)
        var month = cal.get(Calendar.MONTH)
        var year = cal.get(Calendar.YEAR)
        var hour = cal.get(Calendar.HOUR)
        var min = cal.get(Calendar.MINUTE)
        month += 1

        var ampm = "AM"

        if (cal.get(Calendar.AM_PM) == 1) {
            ampm = "PM"
        }

        if (hour < 10) {
            Time += "0"
        }
        Time += hour
        Time += ":"

        if (min < 10) {
            Time += "0"
        }

        Time += min
        Time += (" $ampm")

        Date = ("$day / $month / $year")

        btn_post.setOnClickListener {
            postRequest()
        }

    }

    private fun validatePostDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etPostPhone.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }

            TextUtils.isEmpty(binding.etPostAddress.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_address), true)
                false
            }

            else -> {
                //showErrorSnackBar(resources.getString(R.string.registery_successfull), false)
                true
            }

        }
    }

    private fun postRequest() {

        if (validatePostDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))

            val username = this.getSharedPreferences(Constants.BLOODBANK_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME,"")!!

            val customUser = CustomUser(
                username,
                binding.etPostPhone.text.toString().trim() { it <= ' '},
                binding.etPostAddress.text.toString().trim() { it <= ' '},
                binding.autoCompleteTextViewPostBloodGroup.text.toString().trim() { it <= ' '},
                binding.autoCompleteTextViewPostState.text.toString().trim() { it <= ' '},
                Time,
                Date
            )

            FireStoreClass().postBloodRequest(this@PostActivity, customUser)

        }
    }

    fun postBloodRequestSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(this@PostActivity, resources.getString(R.string.post_blood_success),
            Toast.LENGTH_SHORT
        ).show()

        finish()
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
}