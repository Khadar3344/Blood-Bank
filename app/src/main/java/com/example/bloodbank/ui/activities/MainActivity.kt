package com.example.bloodbank.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.bloodbank.R
import com.example.bloodbank.databinding.ActivityMainBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.User
import com.example.bloodbank.ui.fragments.*
import com.example.bloodbank.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.nav_header_dashboard.view.*

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var mUser: User
    lateinit var uid: String


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setSupportActionBar(toolbar)
        binding.apply {

            fab.setOnClickListener {
                startActivity(Intent(this@MainActivity, PostActivity::class.java))
            }
            toggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()


            supportActionBar?.setDisplayHomeAsUpEnabled(true)


            auth = FirebaseAuth.getInstance()
            uid = auth.currentUser?.uid.toString()

            firestore = FirebaseFirestore.getInstance()

            firestore.collection(Constants.USERS)


            if (uid.isNotEmpty()) {

                getUserDetails()
            }


            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
                navView.menu.getItem(0).isChecked = true
            }

            navView.getHeaderView(0)

            navView.setNavigationItemSelectedListener {
                it.isChecked = true

                when(it.itemId) {
                    R.id.home -> {
                        replaceFragment(HomeFragment(), it.title.toString())
                    }
                    R.id.userprofile -> {
                        startActivity(Intent(applicationContext, UserProfileActivity::class.java))
                    }
                    R.id.user_achiev -> {
                        replaceFragment(AchievementsFragment(), it.title.toString())
                    }
                    R.id.blood_storage -> {
                       replaceFragment(SearchDonorFragment(), it.title.toString())
                    }
                    R.id.nearby_hospital -> {
                        replaceFragment(NearestHospitalFragment(), it.title.toString())
                    }
                    R.id.logout -> {
                        auth.signOut()
                        startActivity(Intent(applicationContext,LoginActivity::class.java))
                        finish()
                    }
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        binding.drawerLayout.closeDrawers()
        fragmentTransaction.commit()
        setTitle(title)
    }


    private fun getUserDetails() {
        firestore.collection(Constants.USERS)
            .document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                mUser = snapshot.toObject(User::class.java)!!

                binding.navView.UserNameView.text = mUser.fullName
                binding.navView.UserEmailView.text = mUser.email
            }
            .addOnFailureListener {

            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.donateinfo -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, BloodInfo()).commit()
            }

            R.id.devinfo -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, AboutUs()).commit()
            }

        }


        if (toggle.onOptionsItemSelected(item)) return true

        return super.onOptionsItemSelected(item)
    }

}