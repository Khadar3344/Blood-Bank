package com.example.bloodbank.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bloodbank.R
import com.example.bloodbank.databinding.ActivityUserProfileBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.Donor
import com.example.bloodbank.models.User
import com.example.bloodbank.ui.fragments.HomeFragment
import com.example.bloodbank.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var mUserDetails: User
    private lateinit var mDonorDetails: Donor
    private lateinit var auth: FirebaseAuth
    private lateinit var mfirestore: FirebaseFirestore
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val sex = resources.getStringArray(R.array.sex)
        val arraySex = ArrayAdapter(this, R.layout.sex_item, sex)
        binding.autoCompleteTextViewSexProfile.setAdapter(arraySex)

        val bloodGroup = resources.getStringArray(R.array.Blood_Group)
        val arrayBlood = ArrayAdapter(this, R.layout.blood_group_item, bloodGroup)
        binding.autoCompleteTextViewBloodGroupProfile.setAdapter(arrayBlood)

        val state = resources.getStringArray(R.array.state_list)
        val arrayState = ArrayAdapter(this, R.layout.state_item, state)
        binding.autoCompleteTextViewStateProfile.setAdapter(arrayState)

        setupActionBar()

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        mfirestore = FirebaseFirestore.getInstance()

        binding.apply {

            mfirestore.collection(Constants.USERS)
                .document(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    mUserDetails = snapshot.toObject(User::class.java)!!
                    showProgressDialog(resources.getString(R.string.please_wait))
                    etProfileFullName.setText(mUserDetails.fullName)
                    autoCompleteTextViewSexProfile.setText(mUserDetails.sex)
                    autoCompleteTextViewBloodGroupProfile.setText(mUserDetails.bloodGroup)
                    etProfilePhone.setText(mUserDetails.mobile)
                    etProfileAddress.setText(mUserDetails.address)
                    autoCompleteTextViewStateProfile.setText(mUserDetails.state)

                    mfirestore.collection(Constants.DONORS)
                        .document(uid)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                cbUnMarkDonor.isChecked = true
                                mDonorDetails = snapshot.toObject(Donor::class.java)!!
                            }
                            else {
                                Toast.makeText(this@UserProfileActivity,
                                    resources.getString(R.string.you_are_not_donor),
                                    Toast.LENGTH_SHORT)
                                    .show()
                            }
                            hideProgressDialog()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Donor", e.message.toString())
                        }
                }
                .addOnFailureListener {  e ->
                    Log.e("User", e.message.toString())
                }







            btnUpdateProfile.setOnClickListener {
                updateUserProfile()
            }
        }


    }

    private fun updateUserProfile() {
        if (validateUserProfile()) {
            showProgressDialog(resources.getString(R.string.please_wait))

            updateProfileDetails()
        }
    }

    private fun updateProfileDetails() {
        val userHashMap = HashMap<String, Any>()
        binding.apply {
            val name = etProfileFullName.text.toString().trim { it <= ' ' }
            if (name.isNotEmpty()) {
                userHashMap[Constants.FULL_NAME] = name
            }

            val sexProfile = autoCompleteTextViewSexProfile.text.toString().trim { it <= ' ' }
            if (sexProfile.isNotEmpty()) {
                userHashMap[Constants.SEX] = sexProfile
            }

            val bloodGroupProfile = autoCompleteTextViewBloodGroupProfile.text.toString().trim { it <= ' ' }
            if (bloodGroupProfile.isNotEmpty()) {
                userHashMap[Constants.BLOOD_GROUP] = bloodGroupProfile
            }

            val phone = etProfilePhone.text.toString().trim { it <= ' ' }
            if (phone.isNotEmpty()) {
                userHashMap[Constants.MOBILE] = phone
            }

            val address = etProfileAddress.text.toString().trim { it <= ' ' }
            if (address.isNotEmpty()) {
                userHashMap[Constants.ADDRESS] = address
            }

            val stateProfile = autoCompleteTextViewStateProfile.text.toString().trim { it <= ' ' }
            if (name.isNotEmpty()) {
                userHashMap[Constants.STATE] = stateProfile
            }


            FireStoreClass().updateUserProfileData(this@UserProfileActivity, userHashMap)
            if (cbUnMarkDonor.isChecked) {
                userHashMap[Constants.LAST_DONATE] = resources.getString(R.string.do_not_donate_yet)
                userHashMap[Constants.TOTAL_DONATE] = 0
                FireStoreClass().updateDonorProfileData(this@UserProfileActivity, userHashMap)
            } else {
                FireStoreClass().deleteDonorData(this@UserProfileActivity)
            }

        }
    }

    private fun validateUserProfile(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etProfileFullName.text.toString().trim() { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_full_name), true)
                false
            }

            TextUtils.isEmpty(binding.etProfilePhone.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }

            TextUtils.isEmpty(binding.etProfileAddress.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_address), true)
                false
            }

            else -> {

                true
            }

        }


    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarUserProfileActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarUserProfileActivity.setNavigationOnClickListener { onBackPressed() }

    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()

    }

}