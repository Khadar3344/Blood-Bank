package com.example.bloodbank.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bloodbank.R
import com.example.bloodbank.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarForgotPasswordActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarForgotPasswordActivity.setNavigationOnClickListener { onBackPressed() }

        binding.btnSubmit.setOnClickListener {
            val email = binding.etEmailForgotPw.text.toString().trim() { it <= ' '}
            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()
                        if (task.isSuccessful) {
                            // Show the toast message and finish the forgot password activity to go to the loading.
                            Toast.makeText(this, resources.getString(R.string.email_sent_success),
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                            binding.etEmailForgotPw.text = null
                        }
                    }
            }
        }
    }
}