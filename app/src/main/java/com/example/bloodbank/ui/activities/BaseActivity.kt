package com.example.bloodbank.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bloodbank.R

import com.google.android.material.snackbar.Snackbar
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity

open class BaseActivity : LocaleAwareCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var mProgressDialog: Dialog

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            this,
                            R.color.colorSnackBarError
                    )
            )
        } else {
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            this,
                            R.color.colorSnackBarSuccess
                    )
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String) {

        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        // Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitPressedOnce = false}, 2000)
    }
}