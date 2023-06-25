package com.example.bloodbank.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bloodbank.R
import com.example.bloodbank.databinding.ActivityRegisterBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.Donor
import com.example.bloodbank.models.User
import com.example.bloodbank.utils.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.request_list_item.*

 class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val sex = resources.getStringArray(R.array.sex)
        val arraySex = ArrayAdapter(this, R.layout.sex_item, sex)
        binding.autoCompleteTextViewSex.setAdapter(arraySex)

        val bloodGroup = resources.getStringArray(R.array.Blood_Group)
        val arrayBlood = ArrayAdapter(this, R.layout.blood_group_item, bloodGroup)
        binding.autoCompleteTextViewBloodGroup.setAdapter(arrayBlood)

        val state = resources.getStringArray(R.array.state_list)
        val arrayState = ArrayAdapter(this, R.layout.state_item, state)
        binding.autoCompleteTextViewState.setAdapter(arrayState)

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

        binding.tvLogin.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }


    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarRegisterActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    }


    /**
     * A function to validate the entries of a new user.
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etFullName.text.toString().trim() { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_full_name), true)
                false
            }

            TextUtils.isEmpty(binding.etPhone.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }

            binding.etPhone.text.toString().trim{ it <= ' '}.length < 10 -> {
                showErrorSnackBar(
                    "رقم الهاتف يجب أن يكون أكبر من 10",
                    true)
                false
            }

            TextUtils.isEmpty(binding.etAddress.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_address), true)
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            binding.etPassword.text.toString().trim{ it <= ' '}.length < 5 -> {
                showErrorSnackBar(
                    "كلمة المرور يجب أن تكون أكبر من 5",
                    true)
                false
            }

            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            binding.etPassword.text.toString().trim { it <= ' ' } != binding.etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false
            }



            else -> {
                //showErrorSnackBar(resources.getString(R.string.registery_successfull), false)
                true
            }


        }
    }

     private fun registerUser() {

        // Check with validate function if the entries are valid or not
        if (validateRegisterDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))

            val email = binding.etEmail.text.toString().trim() { it <= ' '}
            val password = binding.etPassword.text.toString().trim() { it <= ' '}

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->


                        // if the register is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                binding.etFullName.text.toString().trim() { it <= ' '},
                                binding.autoCompleteTextViewSex.text.toString().trim() { it <= ' '},
                                binding.autoCompleteTextViewBloodGroup.text.toString().trim() { it <= ' '},
                                binding.etPhone.text.toString().trim() { it <= ' '},
                                binding.etAddress.text.toString().trim() { it <= ' '},
                                binding.autoCompleteTextViewState.text.toString().trim() { it <= ' '},
                                binding.etEmail.text.toString().trim() { it <= ' '}
                            )

                            FireStoreClass().registerUser(this@RegisterActivity, user)

                            if (binding.cbBeDonor.isChecked) {
                                val donor = Donor(
                                    firebaseUser.uid,
                                    binding.etFullName.text.toString().trim() { it <= ' '},
                                    binding.autoCompleteTextViewSex.text.toString().trim() { it <= ' '},
                                    binding.autoCompleteTextViewBloodGroup.text.toString().trim() { it <= ' '},
                                    binding.etPhone.text.toString().trim() { it <= ' '},
                                    binding.etAddress.text.toString().trim() { it <= ' '},
                                    binding.autoCompleteTextViewState.text.toString().trim() { it <= ' '},
                                    resources.getString(R.string.do_not_donate_yet),
                                    0
                                )

                                FireStoreClass().registerDonor(this@RegisterActivity,donor)
                            }
                        } else {
                            hideProgressDialog()
                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })

        }

    }

    fun userRegistrationSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(this,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun donorRegistrationSuccess() {
        hideProgressDialog()

        Toast.makeText(this,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()
    }
}