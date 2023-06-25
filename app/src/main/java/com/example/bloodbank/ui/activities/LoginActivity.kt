package com.example.bloodbank.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.bloodbank.R
import com.example.bloodbank.databinding.ActivityLoginBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.Admin
import com.example.bloodbank.models.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Click event assigned to Forgot Password text.
        binding.tvForgotPassword.setOnClickListener(this)
        // Click event assigned to Login Button.
        binding.btnLogin.setOnClickListener(this)
        // Click event assigned to Register text.
        binding.tvRegister.setOnClickListener(this)
    }

   /* fun adminLoggedInSuccess(admin: Admin) {

        // Hide the progress dialog.
        hideProgressDialog()
        val intent = Intent(this@LoginActivity,AdminMainActivity::class.java)
        startActivity(intent)
    }*/

    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()
        val intent = Intent(this@LoginActivity,MainActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.tv_forgot_password -> {
                    // Launch the forgot password screen when the user clicks on the text.
                    val intent = Intent(this, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    loginAdmin()
//                    loginRegisteredUser()
                }

                R.id.tv_register -> {
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails() : Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim() { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim() { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                true
            }

        }
    }

    private fun loginRegisteredUser() {

        if (validateLoginDetails()) {


            // Show the progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text editText and trim the space
            val email = binding.etEmail.text.toString().trim() { it <= ' '}
            val password = binding.etPassword.text.toString().trim() { it <= ' '}

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

//                                FireStoreClass().getAdminDetails(this@LoginActivity)

                        FireStoreClass().getUserDetails(this@LoginActivity)

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }


        }
    }


    private fun loginAdmin () {
        val admin = Admin()
        if (validateLoginDetails()) {

            binding.apply {
//            showProgressDialog(resources.getString(R.string.please_wait))

                val email = binding.etEmail.text.toString().trim() { it <= ' '}
                val password = binding.etPassword.text.toString().trim() { it <= ' '}

                if (email == admin.email_admin && password == admin.password) {
                    startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
                    Toast.makeText(
                        this@LoginActivity,
                        "تم تسجيل دخول المدير بنجاح",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
//                    hideProgressDialog()
                    loginRegisteredUser()
                }
            }
        }
    }
}