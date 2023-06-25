package com.example.bloodbank.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bloodbank.R
import com.example.bloodbank.databinding.FragmentAchievementsBinding
import com.example.bloodbank.firestore.FireStoreClass
import com.example.bloodbank.models.Donor
import com.example.bloodbank.models.User
import com.example.bloodbank.ui.activities.LoginActivity
import com.example.bloodbank.ui.activities.MainActivity
import com.example.bloodbank.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AchievementsFragment : BaseFragment() {

    private lateinit var binding: FragmentAchievementsBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var mUser: User
    private lateinit var mDonor: Donor
    private lateinit var uid: String
    private lateinit var lastData: String
    private lateinit var calendar: Calendar
    private lateinit var bloodGroup: Array<String>
    private lateinit var state: Array<String>

    private var  cur_day:Int = 0
    private  var cur_month:Int = 0
    private  var cur_year:Int = 0
    private  var day:Int = 0
    private  var month:Int = 0
    private  var year:Int = 0
    private  var totday:Int = 0
    private lateinit var getState: String
    private lateinit var getBloopGroup: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAchievementsBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        showProgressDialog(resources.getString(R.string.please_wait))
        bloodGroup = resources.getStringArray(R.array.Blood_Group)
        state = resources.getStringArray(R.array.state_list)


        mAuth = FirebaseAuth.getInstance()
        uid = mAuth.currentUser?.uid.toString()
        firestore = FirebaseFirestore.getInstance()

        firestore.collection(Constants.USERS)
            .document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    mUser = snapshot.toObject(User::class.java)!!
                     getState = mUser.state
                    getBloopGroup = mUser.bloodGroup
                    firestore.collection(Constants.DONORS)
                        .document(uid)
                        .get()
                        .addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                mDonor = dataSnapshot.toObject(Donor::class.java)!!
                                binding.totalDonated.text = "${mDonor.totalDonate}  مرة"
                                if (mDonor.totalDonate == 0) {
                                    lastData = "01/01/2010"
                                    binding.lastDonated.text = resources.getString(R.string.do_not_donate_yet)
                                } else {
                                    lastData = mDonor.lastDonate
                                    binding.lastDonated.text = mDonor.lastDonate
                                }

                                if (lastData.isNotEmpty()) {

                                    var cnt = 0
                                    var tot = 0


                                    for (i in lastData) {
                                        if (cnt == 0 && i == '/') {
                                            day = tot
                                            tot = 0
                                            cnt += 1
                                        } else if (cnt == 1 && i == '/') {
                                            cnt += 1
                                            month = tot
                                            tot = 0
                                        } else tot = tot * 10 + (i - '0')
                                    }

                                    year = tot

                                    calendar = Calendar.getInstance(TimeZone.getDefault())
                                    cur_day = calendar.get(Calendar.DAY_OF_MONTH)
                                    cur_month = calendar.get(Calendar.MONTH) + 1
                                    cur_year = calendar.get(Calendar.YEAR)

                                    if (day > cur_day) {
                                        cur_day += 30
                                        cur_month -= 1
                                    }
                                    totday += (cur_day - day)

                                    if (month > cur_month) {
                                        cur_month += 12
                                        cur_year -= 1
                                    }

                                    totday += ((cur_month - month) * 30)

                                    totday += ((cur_year - year) * 365)

                                    if (totday > 120) {

                                        binding.apply {

                                            tvDonateInfo.text = resources.getString(R.string.have_you_donated_today)
                                            nextDonate.visibility = View.GONE
                                            btnYesAchievement.visibility = View.VISIBLE
                                            btnNoAchievement.visibility = View.VISIBLE

                                            cur_day = calendar.get(Calendar.DAY_OF_MONTH)
                                            cur_month = calendar.get(Calendar.MONTH) + 1
                                            cur_year = calendar.get(Calendar.YEAR)

                                            btnYesAchievement.setOnClickListener {
                                                showProgressDialog(resources.getString(R.string.please_wait))
                                                agreeAchievement()
                                            }

                                            btnNoAchievement.setOnClickListener {
                                                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer,BloodInfo())?.commit()
                                            }
                                        }
                                    } else {
                                        binding.apply {
                                            tvDonateInfo.text = resources.getString(R.string.next_donate)
                                            btnYesAchievement.visibility = View.GONE
                                            btnNoAchievement.visibility = View.GONE
                                            nextDonate.visibility = View.VISIBLE
                                            nextDonate.text = "${(120 - totday)}  يوم"
                                        }
                                    }
                                }
                            } else {
                                binding.donorAchieve.visibility = View.GONE
                                binding.showInfo.visibility = View.VISIBLE
                                Toast.makeText(context, resources.getString(R.string.update_your_profile_to_be_donor), Toast.LENGTH_SHORT).show()
                            }

                            hideProgressDialog()
                        }
                        .addOnFailureListener {  }
                } else {
                    Toast.makeText(context, "You are not a user ${state[0]} ${bloodGroup[0]}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Log.d("User", e.message.toString())
            }
    }

    private fun agreeAchievement() {
        val donorHashMap = HashMap<String, Any>()

        binding.apply {
            donorHashMap[Constants.STATE] = getState
            donorHashMap[Constants.BLOOD_GROUP] = getBloopGroup
            donorHashMap[Constants.LAST_DONATE] = "$cur_day/$cur_month/$cur_year"
            donorHashMap[Constants.TOTAL_DONATE] = mDonor.totalDonate + 1
        }

        FireStoreClass().donorAgreeAchievement(this@AchievementsFragment, donorHashMap)
    }

    fun donorAchievementSuccess() {
        hideProgressDialog()

        Toast.makeText(requireContext(),
            resources.getString(R.string.donate_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

}